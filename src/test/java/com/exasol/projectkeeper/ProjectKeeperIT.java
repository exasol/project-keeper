package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.shared.utils.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.validators.TestMavenModel;

/**
 * This integration test tests the maven plugin in a safe environment. Since we don't want to install the plugin to the
 * user's maven repository, it creates a temporary maven home, and installs the plugin there. Then the test creates a
 * temporary project, runs the plugin on that project and checks the output.
 */
@Tag("integration")
class ProjectKeeperIT {
    private static final File PLUGIN = Path.of("target", "project-keeper-maven-plugin-0.6.0.jar").toFile();
    private static final File PLUGIN_POM = Path.of("pom.xml").toFile();
    /**
     * When you enable debugging here, connect with a debugger to localhost:8000 during the test run. Since the tests
     * wait for the debugger, this should be disabled on commits so that CI runs through.
     */
    private static final boolean DEBUG = false;
    private static final String TEST_PROJECT = "/test_project";

    @TempDir
    static Path mavenRepo;

    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    private Path eachTestsTemp;
    private Path projectDir;

    @BeforeAll
    static void beforeAll() throws VerificationException, IOException {
        installThisPluginToTestMavenRepo();
    }

    private static void installThisPluginToTestMavenRepo() throws IOException, VerificationException {
        final Path testProjectDir = Files.createTempDirectory("pk-test");
        final File testDir = ResourceExtractor.extractResourcePath(ProjectKeeperIT.class, TEST_PROJECT,
                testProjectDir.toFile(), true);
        try {
            final Verifier verifier = new Verifier(testDir.getAbsolutePath());
            verifier.setCliOptions(List.of(//
                    "-Dfile=" + PLUGIN.getAbsolutePath(), //
                    "-DlocalRepositoryPath=" + mavenRepo.toAbsolutePath(), //
                    "-DpomFile=" + PLUGIN_POM.getAbsolutePath()));
            verifier.executeGoal("install:install-file");
            verifier.verifyErrorFreeLog();
        } finally {
            org.apache.commons.io.FileUtils.deleteDirectory(testProjectDir.toFile());
        }
    }

    @BeforeEach
    void beforeEach() throws IOException, GitAPIException {
        this.eachTestsTemp = Files.createTempDirectory("pk-test");
        this.projectDir = ResourceExtractor
                .extractResourcePath(ProjectKeeperIT.class, TEST_PROJECT, this.eachTestsTemp.toFile(), true).toPath();
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    @AfterEach
    void afterEach() throws IOException {
        FileUtils.deleteDirectory(this.eachTestsTemp.toFile());
    }

    @Test
    void testVerify() throws VerificationException {
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-6")),
                () -> assertThat(output,
                        containsString("E-PK-17: Missing required: '.settings/org.eclipse.jdt.core.prefs'")),
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin.")),
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.sonatype.ossindex.maven:ossindex-maven-plugin.")),
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-enforcer-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin com.exasol:artifact-reference-checker-maven-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-surefire-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-assembly-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.jacoco:jacoco-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-failsafe-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-deploy-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-gpg-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-source-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-javadoc-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-dependency-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-26: '.github/workflows/maven.yml' exists but must not exist.")), //
                () -> assertThat(output, containsString("E-PK-29: Missing dependency 'org.jacoco:org.jacoco.agent'.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.sonatype.plugins:nexus-staging-maven-plugin.")), //
                () -> assertThat(output, not(containsString("logging.properties")))//
        );
    }

    @Test
    void testOnlyPomInvalid() throws IOException, VerificationException {
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("test_project/pom.xml")),
                this.projectDir.resolve("pom.xml"), StandardCopyOption.REPLACE_EXISTING);
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-6")),
                () -> assertThat(output,
                        not(containsString("E-PK-17: Missing required: .settings/org.eclipse.jdt.core.prefs"))),
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))//
        );
    }

    @Test
    void testFix() throws VerificationException {
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        verifier.verifyErrorFreeLog();
    }

    @Test
    void testValidAfterFix() throws VerificationException {
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        assertDoesNotThrow(() -> verifier.executeGoal("project-keeper:verify"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testChangesFileGeneration(final boolean released) throws IOException, GitAPIException, VerificationException {
        setupDemoProjectWithDependencyChange(released);
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        final String generatedChangesFile = Files.readString(this.projectDir.resolve("doc/changes/changes_0.2.0.md"));
        assertAll(//
                () -> assertThat(generatedChangesFile, startsWith("# my-test-project 0.2.0, released")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `com.example:my-lib:1.0.0` to `1.0.1`")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `org.apache.maven.plugins:maven-surefire-plugin:2.12.4` to")));
    }

    private void setupDemoProjectWithDependencyChange(final boolean released) throws IOException, GitAPIException {
        final Git git = Git.open(this.projectDir.toFile());
        writePomWithOneDependency("0.1.0", "1.0.0");
        makeRelease(git, "0.1.0");
        writePomWithOneDependency("0.2.0", "1.0.1");
        if (released) {
            makeRelease(git, "0.2.0");
        }
    }

    private void makeRelease(final Git git, final String releaseTag) throws GitAPIException {
        git.add().addFilepattern("pom.xml").call();
        git.commit().setMessage("commit for release " + releaseTag).call();
        git.tag().setName(releaseTag).call();
    }

    private void writePomWithOneDependency(final String pomVersion, final String dependencyVersion) throws IOException {
        final TestMavenModel testMavenModel = getTestMavenModelWithProjectKeeperPlugin();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency(dependencyVersion);
        testMavenModel.writeAsPomToProject(this.projectDir);
    }

    private TestMavenModel getTestMavenModelWithProjectKeeperPlugin() {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.addProjectKeeperPlugin(List.of(ProjectKeeperModule.DEFAULT));
        return testMavenModel;
    }

    @Test
    void testJacocoAgentIsExtracted() throws VerificationException {
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoal("package");
        assertThat(this.projectDir.resolve(Path.of("target", "jacoco-agent", "org.jacoco.agent-runtime.jar")).toFile(),
                anExistingFile());
    }

    private Verifier getVerifier() throws VerificationException {
        final Verifier verifier = new Verifier(this.projectDir.toFile().getAbsolutePath());
        verifier.setLocalRepo(mavenRepo.toAbsolutePath().toString());
        if (DEBUG) {
            verifier.setDebug(true);
            verifier.setDebugJvm(true);
        }
        return verifier;
    }
}
