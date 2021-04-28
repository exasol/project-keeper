package com.exasol.projectkeeper;

import static com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter.getCurrentProjectVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.apache.maven.shared.utils.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.ProjectKeeperPluginDeclaration;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {
    private static final Logger LOGGER = Logger.getLogger(ProjectKeeperAbstractIT.class.getName());
    protected static final String CURRENT_VERSION = getCurrentProjectVersion();
    private static final File PLUGIN = Path.of("target", "project-keeper-maven-plugin-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File PLUGIN_POM = Path.of("pom.xml").toFile();
    /**
     * When you enable debugging here, connect with a debugger to localhost:8000 during the test run. Since the tests
     * wait for the debugger, this should be disabled on commits so that CI runs through.
     */
    private static final boolean DEBUG = false;
    private static final String TEST_PROJECT = "/test_project";

    static Path mavenRepo;

    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    protected Path projectDir;

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
        final Path tmpProjectDir = Files.createTempDirectory("pk-test");
        new TestMavenModel().writeAsPomToProject(tmpProjectDir);
        try {
            final Verifier verifier = new Verifier(tmpProjectDir.toString());
            verifier.setCliOptions(List.of(//
                    "-Dfile=" + PLUGIN.getAbsolutePath(), //
                    "-DlocalRepositoryPath=" + mavenRepo.toAbsolutePath(), //
                    "-DpomFile=" + PLUGIN_POM.getAbsolutePath()));
            verifier.executeGoal("install:install-file");
            verifier.verifyErrorFreeLog();
        } finally {
            org.apache.commons.io.FileUtils.deleteDirectory(tmpProjectDir.toFile());
        }
    }

    private static void removeProjectKeeperFromRepository() throws IOException {
        FileUtils.deleteDirectory(mavenRepo.resolve(Path.of("com", "exasol", "project-keeper")).toFile());
    }

    @BeforeEach
    void beforeEach() throws IOException, GitAPIException {
        this.projectDir = Files.createTempDirectory("pk-test");
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    @AfterEach
    void afterEach() throws IOException {
        FileUtils.deleteDirectory(this.projectDir.toFile());
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
        testMavenModel.addProjectKeeperPlugin(new ProjectKeeperPluginDeclaration(CURRENT_VERSION));
        return testMavenModel;
    }

    private static int jacocoAgentCounter = 0;

    protected Verifier getVerifier() throws VerificationException {
        final Verifier verifier = new Verifier(this.projectDir.toFile().getAbsolutePath());
        verifier.setLocalRepo(mavenRepo.toAbsolutePath().toString());
        if (DEBUG) {
            verifier.setDebug(true);
            verifier.setDebugJvm(true);
        }
        addJacocoAgent(verifier);
        return verifier;
    }

    private void addJacocoAgent(final Verifier verifier) {
        final var agentPath = Path.of("target", "jacoco-agent", "org.jacoco.agent-runtime.jar").toAbsolutePath();
        if (!agentPath.toFile().exists()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-56").message(
                    "Could not find jacoco agent at {{path}}. The agent is exported by the maven-dependency-plugin during build.")
                    .mitigation("Run `mvn package`.").toString());
        }
        final var reportPath = Path.of("target", "jacoco-mvn-" + jacocoAgentCounter + ".exec").toAbsolutePath();
        jacocoAgentCounter++;
        final String jacocoAgentParameter = "-javaagent:" + agentPath + "=output=file,destfile=" + reportPath;
        verifier.setEnvironmentVariable("MAVEN_OPTS", jacocoAgentParameter);
    }
}
