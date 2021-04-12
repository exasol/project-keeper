package com.exasol.projectkeeper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.it.util.ResourceExtractor;
import org.apache.maven.shared.utils.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;

import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {
    private static final Logger LOGGER = Logger.getLogger(ProjectKeeperAbstractIT.class.getName());
    private static final String CURRENT_VERSION = "0.7.0"; // todo add automatically
    private static final File PLUGIN = Path.of("target", "project-keeper-maven-plugin-0.7.0.jar").toFile();
    private static final File PLUGIN_POM = Path.of("pom.xml").toFile();
    /**
     * When you enable debugging here, connect with a debugger to localhost:8000 during the test run. Since the tests
     * wait for the debugger, this should be disabled on commits so that CI runs through.
     */
    private static final boolean DEBUG = false;
    private static final String TEST_PROJECT = "/test_project";

    static Path mavenRepo;
    protected Path projectDir;
    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    private Path eachTestsTemp;

    @BeforeAll
    static void beforeAll() throws VerificationException, IOException {
        printDebuggerWarning();
        createTemporaryLocalRepositoryIfNotExist();
        ProjectKeeperAbstractIT.installThisPluginToTestMavenRepo();
    }

    private static void printDebuggerWarning() {
        if (DEBUG) {
            LOGGER.warning(
                    "Debugging is enabled. Please connect to localhost:8000 using the debugger of your IDE. The tests will wait until the debugger is connected.");
        }
    }

    private static void createTemporaryLocalRepositoryIfNotExist() {
        mavenRepo = Path.of(System.getProperty("java.io.tmpdir"))
                .resolve("project-keeper-integration-test-maven-repository");
        if (!mavenRepo.toFile().exists()) {
            mavenRepo.toFile().mkdir();
        }
    }

    private static void installThisPluginToTestMavenRepo() throws IOException, VerificationException {
        removeProjectKeeperFromRepository();
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

    private static void removeProjectKeeperFromRepository() throws IOException {
        FileUtils.deleteDirectory(mavenRepo.resolve(Path.of("com", "exasol", "project-keeper")).toFile());
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

    protected void writePomWithOneDependency(final String pomVersion, final String dependencyVersion)
            throws IOException {
        final TestMavenModel testMavenModel = getTestMavenModelWithProjectKeeperPlugin();
        testMavenModel.setVersion(pomVersion);
        testMavenModel.addDependency("error-reporting-java", "com.exasol", "compile", dependencyVersion);
        testMavenModel.writeAsPomToProject(this.projectDir);
    }

    protected TestMavenModel getTestMavenModelWithProjectKeeperPlugin() {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.addProjectKeeperPlugin(List.of(ProjectKeeperModule.DEFAULT), CURRENT_VERSION);
        return testMavenModel;
    }

    protected Verifier getVerifier() throws VerificationException {
        final Verifier verifier = new Verifier(this.projectDir.toFile().getAbsolutePath());
        verifier.setLocalRepo(mavenRepo.toAbsolutePath().toString());
        if (DEBUG) {
            verifier.setDebug(true);
            verifier.setDebugJvm(true);
        }
        return verifier;
    }
}
