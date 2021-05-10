package com.exasol.projectkeeper;

import static com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter.getCurrentProjectVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

<<<<<<< HEAD
=======
import org.apache.commons.io.FileUtils;
import org.apache.maven.it.VerificationException;
>>>>>>> main
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.validators.ProjectKeeperPluginDeclaration;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {
    protected static final String CURRENT_VERSION = getCurrentProjectVersion();
    private static final File PLUGIN = Path.of("target", "project-keeper-maven-plugin-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File PLUGIN_POM = Path.of("pom.xml").toFile();

    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    protected Path projectDir;
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = new MavenIntegrationTestEnvironment();
        mavenIntegrationTestEnvironment.installPlugin(PLUGIN, PLUGIN_POM);
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
