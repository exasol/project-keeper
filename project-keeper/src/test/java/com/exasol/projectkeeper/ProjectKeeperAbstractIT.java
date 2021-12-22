package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.TestEnvBuilder.CURRENT_VERSION;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.validators.ProjectKeeperPluginDeclaration;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {

    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    protected Path projectDir;
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = TestEnvBuilder.getTestEnv();
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

    protected Verifier getVerifier() {
        return mavenIntegrationTestEnvironment.getVerifier(this.projectDir);
    }
}
