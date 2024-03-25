package com.exasol.projectkeeper.plugin;

import static com.exasol.projectkeeper.plugin.TestEnvBuilder.CURRENT_VERSION;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.model.Model;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.pom.PomFileIO;

class ProjectKeeperMojoIT {
    private static final String ORIGINAL_SLF4J_VERSION = "1.7.36";
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;
    private static final Logger LOG = Logger.getLogger(ProjectKeeperMojoIT.class.getName());

    @TempDir
    protected Path projectDir;
    private Verifier verifier;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = TestEnvBuilder.getTestEnv();
    }

    @BeforeEach
    void beforeEach(final TestInfo test) throws IOException, GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
        new MvnProjectWithProjectKeeperPluginWriter(CURRENT_VERSION) //
                .addDependency("org.slf4j", "slf4j-api", ORIGINAL_SLF4J_VERSION) //
                .setArtifactFinalName("dummy-${project.version}") //
                .writeAsPomToProject(this.projectDir);
        LOG.info(() -> "Running test " + test.getDisplayName() + " using project " + this.projectDir + "...");
        verifier = mavenIntegrationTestEnvironment.getVerifier(this.projectDir);
    }

    @AfterEach
    void logMavenOutput(final TestInfo test) throws VerificationException {
        verifier.resetStreams();
        final List<String> lines = verifier.loadFile(verifier.getBasedir(), verifier.getLogFileName(), false);
        LOG.info(() -> "Maven log output for test " + test.getDisplayName() + ": " + lines.size() + " lines\n"
                + lines.stream().collect(joining("\n")));
    }

    @Test
    // [itest->dsn~mvn-verify-goal~1]
    void testVerify() throws IOException {
        Files.writeString(this.projectDir.resolve("LICENSE"), "My License\n");
        writeProjectKeeperConfig("sources:\n" + //
                "  - type: maven\n" + //
                "    path: pom.xml\n");
        final VerificationException exception = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = exception.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("Validation failed. See above.")),
                () -> assertThat(output, containsString("[ERROR] E-PK-CORE-17"))//
        );
    }

    @Test
    void testJacocoAgentIsExtracted() throws VerificationException, IOException {
        writeProjectKeeperConfig("sources:\n" + //
                "  - type: maven\n" + //
                "    path: pom.xml\n" + //
                "    modules:\n" + //
                "      - integration_tests\n" + //
                "      - udf_coverage\n");
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoal("package");
        assertThat(projectDir.resolve("target/jacoco-agent/org.jacoco.agent-runtime.jar").toFile(), anExistingFile());
    }

    // [itest->dsn~verify-release-mode.verify-release-date~1]
    // [itest->dsn~verify-release-mode.verify~1]
    @Test
    void testVerifyRelease() throws VerificationException, IOException {
        writeProjectKeeperConfig("sources:\n" + //
                "  - type: maven\n" + //
                "    path: pom.xml\n");
        verifier.executeGoal("project-keeper:fix");
        final VerificationException exception = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify-release"));
        final String output = exception.getMessage();
        final int thisYear = LocalDate.now().getYear();
        assertThat(output,
                containsString("E-PK-CORE-182: Release date '" + thisYear + "-??-??' has invalid format in "));
    }

    // [itest->dsn~dependency-updater.increment-version~2]
    // [itest->dsn~dependency-updater.update-dependencies~1]
    // [itest->dsn~dependency-updater.read-vulnerability-info~1]
    // [itest->dsn~dependency-updater.update-changelog~1]
    @Test
    @DisabledOnOs(OS.WINDOWS) // Passing multi-line vulnerability JSONL via system property fails on Windows
    void testUpgradeDependencies() throws VerificationException, IOException {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                    modules:
                      - jar_artifact
                """);
        final Path userGuidePath = projectDir.resolve("user_guide.md");
        writeFile(userGuidePath, "artifact reference: dummy-0.1.0.jar");

        setVulnerabilityInfo("""
                {
                    "cve": "CVE-2017-10355",
                    "cwe": "CWE-833",
                    "description": "sonatype-2017-0348 - xerces:xercesImpl - Denial of Service (DoS)",
                    "coordinates": "xerces:xercesImpl:jar:2.12.2:test",
                    "references": ["https://link1", "https://link2"],
                    "issue_url": "https://github.com/exasol/testing-release-robot/issues/709"
                }""");
        verifier.executeGoal("project-keeper:fix");
        assertThat("original version", readPom().getVersion(), equalTo("0.1.0"));

        updateReleaseDate("0.1.0", "2023-01-01");
        verifier.verify(true);
        createGitTag("0.1.0");

        verifier.executeGoal("project-keeper:update-dependencies");
        verifier.verify(true);

        final Model updatedPom = readPom();
        final String newVersion = "0.1.1";
        final String updatedSlf4jVersion = updatedPom.getDependencies().get(0).getVersion();
        assertAll(() -> assertThat("incremented version", updatedPom.getVersion(), equalTo(newVersion)),
                () -> assertThat("updated SLF4J version", updatedSlf4jVersion,
                        allOf(not(equalTo(ORIGINAL_SLF4J_VERSION)), not(startsWith("1.")), startsWith("2."))),
                () -> assertContent(userGuidePath, startsWith("artifact reference: dummy-0.1.1.jar")),
                () -> assertContent(ChangesFile.getPathForVersion(newVersion), allOf(
                        startsWith("# My Test Project 0.1.1, released 2024-??-??"),
                        containsString(
                                "Code name: Fixed vulnerability CVE-2017-10355 in xerces:xercesImpl:jar:2.12.2:test"),
                        containsString("""
                                ### CVE-2017-10355 (CWE-833) in dependency `xerces:xercesImpl:jar:2.12.2:test`
                                sonatype-2017-0348 - xerces:xercesImpl - Denial of Service (DoS)
                                #### References
                                * https://link1
                                * https://link2"""), containsString(
                                """
                                        ## Security

                                        * #709: Fixed vulnerability CVE-2017-10355 in dependency `xerces:xercesImpl:jar:2.12.2:test`"""))));
    }

    private void createGitTag(final String tagName) {
        try (Git git = Git.open(projectDir.toFile())) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage("Release " + tagName).call();
            git.tag().setName(tagName).call();
        } catch (GitAPIException | IOException exception) {
            throw new IllegalStateException("Failed to create Git tag " + tagName, exception);
        }
    }

    private void setVulnerabilityInfo(final String... vulnerabilityInfoJson) {
        final String jsonlContent = Arrays.stream(vulnerabilityInfoJson) //
                .map(this::removeLineBreaks) //
                .collect(joining("\n"));
        verifier.setSystemProperty("project-keeper:vulnerabilities", jsonlContent);
    }

    private String removeLineBreaks(final String multiLineText) {
        return multiLineText.replace("\n", "");
    }

    private void updateReleaseDate(final String changeLogVersion, final String newReleaseDate) {
        final Path changelogPath = projectDir.resolve("doc/changes/changes_" + changeLogVersion + ".md");
        try {
            final String content = Files.readString(changelogPath);
            final String updatedContent = content.replace(LocalDate.now().getYear() + "-??-??", newReleaseDate);
            assertThat(updatedContent, not(equalTo(content)));
            Files.writeString(changelogPath, updatedContent);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Error updating release date in " + changelogPath, exception);
        }
    }

    private Model readPom() {
        return new PomFileIO().readPom(projectDir.resolve("pom.xml"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "verify", "fix", "update-dependencies", "verify-release" })
    void testSkip(final String goal) throws IOException, VerificationException {
        writeProjectKeeperConfig("sources:\n" + //
                "  - type: maven\n" + //
                "    path: pom.xml\n");
        verifier.setSystemProperty("project-keeper.skip", "true");
        verifier.executeGoal("project-keeper:" + goal);
        verifier.verifyTextInLog("Skipping project-keeper.");
    }

    private void writeProjectKeeperConfig(final String content) throws IOException {
        writeFile(this.projectDir.resolve(".project-keeper.yml"), content);
    }

    private void writeFile(final Path path, final String content) throws IOException {
        Files.writeString(path, content);
    }

    private void assertContent(Path path, final Matcher<String> contentMatcher) throws IOException {
        if (!path.isAbsolute()) {
            path = projectDir.resolve(path);
        }
        final String changesContent = Files.readString(path);
        assertThat("content of file " + path, changesContent, contentMatcher);
    }
}
