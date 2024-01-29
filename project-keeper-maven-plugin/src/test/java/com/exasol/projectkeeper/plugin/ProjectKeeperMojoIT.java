package com.exasol.projectkeeper.plugin;

import static com.exasol.projectkeeper.plugin.TestEnvBuilder.CURRENT_VERSION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;

class ProjectKeeperMojoIT {
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;
    @TempDir
    protected Path projectDir;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = TestEnvBuilder.getTestEnv();
    }

    @BeforeEach
    void beforeEach() throws IOException, GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
        new MvnProjectWithProjectKeeperPluginWriter(CURRENT_VERSION).writeAsPomToProject(this.projectDir);
    }

    @Test
    // [itest->dsn~mvn-verify-goal~1]
    void testVerify() throws IOException {
        Files.writeString(this.projectDir.resolve("LICENSE"), "My License\n");
        Files.writeString(this.projectDir.resolve(".project-keeper.yml"), //
                "sources:\n" + //
                        "  - type: maven\n" + //
                        "    path: pom.xml\n");
        final Verifier verifier = getVerifier();
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
        Files.writeString(this.projectDir.resolve(".project-keeper.yml"), //
                "sources:\n" + //
                        "  - type: maven\n" + //
                        "    path: pom.xml\n" + //
                        "    modules:\n" + //
                        "      - integration_tests\n" + //
                        "      - udf_coverage\n");
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoal("package");
        assertThat(this.projectDir.resolve(Path.of("target", "jacoco-agent", "org.jacoco.agent-runtime.jar")).toFile(),
                anExistingFile());
    }

    @Test
    void testUpgradeDependencies() throws VerificationException, IOException {
        final PrintStream out = System.out;
        Files.writeString(this.projectDir.resolve(".project-keeper.yml"), //
                "sources:\n" + //
                        "  - type: maven\n" + //
                        "    path: pom.xml\n");
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        assertThat("original version", readPom().getVersion(), equalTo("0.1.0"));

        updateReleaseDate("0.1.0", "2023-01-01");
        verifier.verify(true);

        verifier.executeGoal("project-keeper:update-dependencies");
        verifier.verify(true);

        final List<String> lines = verifier.loadFile(verifier.getBasedir(), verifier.getLogFileName(), false);
        out.println("Got " + lines.size() + " lines:");
        lines.forEach(out::println);
        assertThat("incremented version", readPom().getVersion(), equalTo("0.1.1"));
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
        final Path path = projectDir.resolve("pom.xml");
        try {
            return new MavenXpp3Reader().read(Files.newBufferedReader(path));
        } catch (IOException | XmlPullParserException exception) {
            throw new IllegalStateException("failed to parse " + path + ": " + exception.getMessage(), exception);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "verify", "fix", "update-dependencies" })
    void testSkip(final String phase) throws IOException, VerificationException {
        Files.writeString(this.projectDir.resolve(".project-keeper.yml"), //
                "sources:\n" + //
                        "  - type: maven\n" + //
                        "    path: pom.xml\n");
        final Verifier verifier = getVerifier();
        verifier.setSystemProperty("project-keeper.skip", "true");
        verifier.executeGoal("project-keeper:" + phase);
        verifier.verifyTextInLog("Skipping project-keeper.");
    }

    protected Verifier getVerifier() {
        return mavenIntegrationTestEnvironment.getVerifier(this.projectDir);
    }
}
