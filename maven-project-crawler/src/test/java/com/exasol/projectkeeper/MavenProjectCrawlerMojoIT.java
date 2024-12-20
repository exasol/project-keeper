package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.*;
import com.exasol.projectkeeper.test.TestMavenModel;

@Tag("integration")
class MavenProjectCrawlerMojoIT {
    private static final MavenIntegrationTestEnvironment TEST_ENV = new MavenIntegrationTestEnvironment();
    private static final String CURRENT_VERSION = MavenProjectVersionGetter
            .getProjectRevision(Path.of("../parent-pom/pom.xml"));

    @TempDir
    Path tempDir;

    @BeforeAll
    static void beforeAll() {
        TEST_ENV.installPlugin(Path
                .of("../shared-model-classes/target/project-keeper-shared-model-classes-" + CURRENT_VERSION + ".jar")
                .toFile(), Path.of("../shared-model-classes/.flattened-pom.xml").toFile());
        TEST_ENV.installPlugin(
                Path.of("target/project-keeper-java-project-crawler-" + CURRENT_VERSION + ".jar").toFile(),
                Path.of("./.flattened-pom.xml").toFile());
    }

    // [itest->dsn~eclipse-prefs-java-version~1]
    // [itest->dsn~customize-release-artifacts-jar~0]
    @Test
    void testCrawlProject() throws GitAPIException, IOException, VerificationException {
        final Path subfolder = this.tempDir.resolve("subfolder").toAbsolutePath();
        Files.createDirectory(subfolder);
        Git.init().setDirectory(subfolder.toFile()).call().close();
        final TestMavenModel testProject = new TestMavenModel();
        testProject.addDependency("error-reporting-java", "com.exasol", "compile", "0.4.1");
        testProject.setJavaVersionProperty("17");
        testProject.configureAssemblyPluginFinalName("java-version-${java.version}-project-version-${project.version}");
        testProject.writeAsPomToProject(subfolder);
        final CrawledMavenProject crawledProject = runCrawler(subfolder);
        final ProjectDependency expectedDependency = ProjectDependency.builder() //
                .type(COMPILE) //
                .name("error-reporting-java") //
                .websiteUrl("https://github.com/exasol/error-reporting-java") //
                .licenses(List.of(new License("MIT", "https://opensource.org/licenses/MIT"))) //
                .build();
        assertAll(//
                () -> assertThat("version", crawledProject.getProjectVersion(),
                        equalTo(TestMavenModel.PROJECT_VERSION)),
                () -> assertThat("dependencies", crawledProject.getProjectDependencies().getDependencies(),
                        Matchers.hasItem(expectedDependency)),
                () -> assertThat("dependency change report",
                        crawledProject.getDependencyChangeReport().getChanges(COMPILE),
                        Matchers.contains(new NewDependency("com.exasol", "error-reporting-java", "0.4.1"))),
                () -> assertThat("java version", crawledProject.getJavaVersion(), equalTo("17")), //
                () -> assertThat("artifact name", crawledProject.getReleaseArtifactName(),
                        equalTo("java-version-17-project-version-0.1.0.jar")) //
        );
    }

    // [itest -> dsn~eclipse-prefs-java-version~1]
    @Test
    void testCrawlProjectWithoutJavaVersion() throws GitAPIException, IOException, VerificationException {
        final Path subfolder = this.tempDir.resolve("subfolder").toAbsolutePath();
        Files.createDirectory(subfolder);
        Git.init().setDirectory(subfolder.toFile()).call().close();
        final TestMavenModel testProject = new TestMavenModel();
        testProject.writeAsPomToProject(subfolder);
        final CrawledMavenProject crawledProject = runCrawler(subfolder);
        assertThat(crawledProject.getJavaVersion(), nullValue());
    }

    @Test
    void testCrawlProjectWithoutAssemblyPlugin() throws GitAPIException, IOException, VerificationException {
        final Path subfolder = this.tempDir.resolve("subfolder").toAbsolutePath();
        Files.createDirectory(subfolder);
        Git.init().setDirectory(subfolder.toFile()).call().close();
        final TestMavenModel testProject = new TestMavenModel();
        testProject.writeAsPomToProject(subfolder);
        final CrawledMavenProject crawledProject = runCrawler(subfolder);
        assertThat(crawledProject.getReleaseArtifactName(), nullValue());
    }

    private CrawledMavenProject runCrawler(final Path projectDir) throws VerificationException, IOException {
        final Verifier verifier = TEST_ENV.getVerifier(this.tempDir);
        verifier.setAutoclean(false);
        final String path = projectDir.resolve("pom.xml").toAbsolutePath().toString().replace("\\", "/");
        verifier.setSystemProperty("projectsToCrawl", path);
        verifier.executeGoal("com.exasol:project-keeper-java-project-crawler:" + CURRENT_VERSION + ":crawl");
        final String output = Files.readString(Path.of(verifier.getBasedir()).resolve(verifier.getLogFileName()));
        final String response = new ResponseCoder().decodeResponse(output);
        verifier.resetStreams();
        return MavenProjectCrawlResult.fromJson(response).getCrawledProjects().get(path);
    }
}
