package com.exasol.projectkeeper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;
import com.exasol.projectkeeper.validators.ProjectKeeperPluginDeclaration;
import com.exasol.projectkeeper.validators.TestMavenModel;

public class ProjectKeeperAbstractIT {
    private static final String PROJECT_ROOT_OFFSET = "../";
    private static final File PARENT_POM = Path.of(PROJECT_ROOT_OFFSET, "parent-pom/pom.xml").toFile();
    protected static final String CURRENT_VERSION = MavenProjectVersionGetter.getProjectRevision(PARENT_POM.toPath());
    private static final File SHARED_MODEL = Path
            .of(PROJECT_ROOT_OFFSET,
                    "sharedModelClasses/target/project-keeper-shared-model-classes-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File JAVA_CRAWLER = Path
            .of(PROJECT_ROOT_OFFSET,
                    "javaProjectCrawler/target/project-keeper-java-project-crawler-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File PLUGIN = Path
            .of(PROJECT_ROOT_OFFSET, "project-keeper/target/project-keeper-maven-plugin-" + CURRENT_VERSION + ".jar")
            .toFile();
    private static final File SHARED_MODEL_POM = Path.of(PROJECT_ROOT_OFFSET, "sharedModelClasses/pom.xml").toFile();
    private static final File JAVA_CRAWLER_POM = Path.of(PROJECT_ROOT_OFFSET, "javaProjectCrawler/pom.xml").toFile();
    private static final File PLUGIN_POM = Path.of(PROJECT_ROOT_OFFSET, "project-keeper/pom.xml").toFile();

    /**
     * TempDir only supports one temp directory per test class. For that we can not use it here again but create and
     * drop it by hand.
     */
    protected Path projectDir;
    private static MavenIntegrationTestEnvironment mavenIntegrationTestEnvironment;

    @BeforeAll
    static void beforeAll() {
        mavenIntegrationTestEnvironment = new MavenIntegrationTestEnvironment();
        mavenIntegrationTestEnvironment.installPluginWithoutJar(PARENT_POM);
        mavenIntegrationTestEnvironment.installPlugin(SHARED_MODEL, SHARED_MODEL_POM,
                "project-keeper-shared-model-classes", "com.exasol", CURRENT_VERSION);
        mavenIntegrationTestEnvironment.installPlugin(JAVA_CRAWLER, JAVA_CRAWLER_POM,
                "project-keeper-java-project-crawler", "com.exasol", CURRENT_VERSION);
        mavenIntegrationTestEnvironment.installPlugin(PLUGIN, PLUGIN_POM, "project-keeper-maven-plugin", "com.exasol",
                CURRENT_VERSION);
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
