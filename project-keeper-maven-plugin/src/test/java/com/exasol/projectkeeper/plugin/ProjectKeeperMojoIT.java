package com.exasol.projectkeeper.plugin;

import static com.exasol.projectkeeper.plugin.TestEnvBuilder.CURRENT_VERSION;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.validators.SecurityMdFileValidator;
import com.exasol.projectkeeper.validators.pom.PomFileIO;

class ProjectKeeperMojoIT {
    private static final String ORIGINAL_SLF4J_VERSION = "1.7.36";
    private static final String THIS_YEAR = String.valueOf(LocalDate.now().getYear());
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
    void beforeEach(final TestInfo test) throws GitAPIException {
        try (final Git git = Git.init().setDirectory(this.projectDir.toFile()).call()) {
            // git-commit-id-maven-plugin needs at least one commit
            git.commit().setMessage("initial commit").setAllowEmpty(true).call();
        }
        new MvnProjectWithProjectKeeperPluginWriter(CURRENT_VERSION)
                .addDependency("org.slf4j", "slf4j-api", ORIGINAL_SLF4J_VERSION)
                .setArtifactFinalName("dummy-${project.version}")
                .withJavaVersion("17")
                .writeAsPomToProject(this.projectDir);
        LOG.info(() -> "Running test " + test.getDisplayName() + " using project " + this.projectDir + "...");
        verifier = mavenIntegrationTestEnvironment.getVerifier(this.projectDir);
        // Ossindex requires credentials, skip it in tests
        verifier.setSystemProperty("ossindexSkip", "true");
        // Tests use the system installed Maven for bootstrapping, which maybe an older version as the Maven Wrapper.
        // Therefore, skip Maven version checks in tests.
        verifier.setSystemProperty("enforcer.skip", "true");
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
        writeSecurityMdFile();
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                """);
        final VerificationException exception = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = exception.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("Validation failed. See above.")),
                () -> assertThat(output, containsString("[ERROR] E-PK-CORE-17"))//
        );
    }

    @Test
    // [itest->dsn~maven-wrapper~1]
    void testFixBootstrapsAndCreatesUsableMavenWrapper() throws Exception {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                """);

        verifier.executeGoal("project-keeper:fix");

        assertAll(() -> assertThat(this.projectDir.resolve("mvnw").toFile(), anExistingFile()),
                () -> assertThat(this.projectDir.resolve("mvnw.cmd").toFile(), anExistingFile()),
                () -> assertThat(this.projectDir.resolve(".mvn/wrapper/maven-wrapper.properties").toFile(),
                        anExistingFile()));
        if (!OsCheck.isWindows()) {
            assertThat(Files.isExecutable(this.projectDir.resolve("mvnw")), equalTo(true));
        }
        assertGeneratedWrapperRuns();
    }

    private void assertGeneratedWrapperRuns() throws IOException {
        final List<String> command = OsCheck.isWindows()
                ? List.of("cmd", "/c", this.projectDir.resolve("mvnw.cmd").toString(), "--version")
                : List.of(this.projectDir.resolve("mvnw").toString(), "--version");
        final Process process = new ProcessBuilder(command).directory(this.projectDir.toFile()).redirectErrorStream(true)
                .start();
        final String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertAll(() -> assertThat(process.waitFor(), equalTo(0)),
                () -> assertThat(output, containsString("Apache Maven")));
    }

    private void writeSecurityMdFile() throws IOException {
        Files.writeString(this.projectDir.resolve("SECURITY.md"),
                new SecurityMdFileValidator(projectDir, projectDir.getFileName().toString()).getTemplate());
    }

    @Test
    void testJacocoAgentIsExtracted() throws VerificationException, IOException {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                    modules:
                      - integration_tests
                      - udf_coverage
                """);
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoal("package");
        assertThat(projectDir.resolve("target/jacoco-agent/org.jacoco.agent-runtime.jar").toFile(), anExistingFile());
    }

    @Test
    void testReproducibleBuild() throws VerificationException, IOException {
        Files.writeString(this.projectDir.resolve("error_code_config.yml"), """
                error-tags:
                  DUMMY:
                    packages:
                      - dummy
                    highest-index: 1
                """);
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                """);
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoals(List.of("clean", "install"));
        verifier.executeGoals(List.of("clean", "verify", "artifact:compare"));
        assertThat(projectDir.resolve("target/my-test-project-0.1.0.buildcompare").toFile(), anExistingFile());
    }

    @Test
    void testReproducibleBuildFails() throws VerificationException, GitAPIException, IOException {
        Files.writeString(this.projectDir.resolve("error_code_config.yml"), """
                error-tags:
                  DUMMY:
                    packages:
                      - dummy
                    highest-index: 1
                """);
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                """);
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoals(List.of("clean", "install"));

        try (Git git = Git.open(projectDir.toFile())) {
            git.commit().setMessage("new commit").setAllowEmpty(true).call();
        }
        final VerificationException exception = assertThrows(VerificationException.class,
                () -> verifier.executeGoals(List.of("clean", "verify", "artifact:compare")));

        assertThat(exception.getMessage(), containsString("Rebuilt artifacts are different from reference"));
        assertThat(projectDir.resolve("target/my-test-project-0.1.0.buildcompare").toFile(), anExistingFile());
    }

    // [itest->dsn~verify-release-mode.verify-release-date~2]
    // [itest->dsn~verify-release-mode.verify~1]
    @Test
    void testVerifyRelease() throws VerificationException, IOException {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                """);
        verifier.executeGoal("project-keeper:fix");
        final VerificationException exception = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify-release"));
        final String output = exception.getMessage();
        final int thisYear = LocalDate.now().getYear();
        assertThat(output,
                containsString("E-PK-CORE-182: Release date '" + thisYear + "-??-??' has invalid format in "));
    }

    @Test
    // [itest->dsn~release-workflow.publish-release-sboms~0]
    void testFixAddsSbomPluginsToGeneratedParentPom() throws VerificationException, IOException {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: pom.xml
                    modules:
                      - jar_artifact
                      - maven_central
                """);
        verifier.executeGoal("project-keeper:fix");
        final Model generatedParentPom = readGeneratedParentPom();
        final List<String> pluginArtifactIds = generatedParentPom.getBuild().getPlugins().stream()
                .map(Plugin::getArtifactId).toList();
        assertAll(
                () -> assertThat(pluginArtifactIds, hasItem("spdx-maven-plugin")),
                () -> assertThat(pluginArtifactIds, hasItem("build-helper-maven-plugin")));
    }

    private Model readGeneratedParentPom() {
        return new PomFileIO().readPom(this.projectDir.resolve("pk_generated_parent.pom"));
    }

    private void writeProjectKeeperConfig(final String content) throws IOException {
        writeFile(this.projectDir.resolve(".project-keeper.yml"), content);
    }

    private void writeFile(final Path path, final String content) throws IOException {
        Files.writeString(path, content);
    }
}
