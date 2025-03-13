package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;
import com.exasol.projectkeeper.test.TestMavenModel;
import com.exasol.projectkeeper.validators.pom.PomFileIO;

@Tag("integration")
class JavaProjectCrawlerRunnerIT {
    private static final String DEPENDENCY_ID = "error-reporting-java";
    private static final String DEPENDENCY_GROUP = "com.exasol";
    private static final String DEPENDENCY_VERSION = "0.4.1";
    private static Path testMavenRepo;

    @BeforeAll
    static void beforeAll() {
        final MavenIntegrationTestEnvironment testEnv = TestEnvBuilder.getTestEnv();
        testMavenRepo = testEnv.getLocalMavenRepository();
    }

    @Test
    void testGetDependencyChanges(@TempDir final Path tempDir) throws GitAPIException {
        Git.init().setDirectory(tempDir.toFile()).call().close();
        final Path pomFile = tempDir.resolve("pom.xml");
        writePomFile(pomFile);
        final MavenProjectCrawlResult result = crawlProject(pomFile);
        final CrawledMavenProject mavenProject = result.getCrawledProjects()
                .get(pomFile.toAbsolutePath().toString().replace("\\", "/"));
        final ProjectDependency expectedDependency = ProjectDependency.builder() //
                .type(COMPILE) //
                .name("error-reporting-java") //
                .websiteUrl("https://github.com/exasol/error-reporting-java") //
                .licenses(List.of(new License("MIT", "https://opensource.org/licenses/MIT"))) //
                .build();
        assertAll(//
                () -> assertThat(mavenProject.getProjectDependencies().getDependencies(),
                        Matchers.hasItem(expectedDependency)),
                () -> assertThat(mavenProject.getDependencyChangeReport().getChanges(Type.COMPILE),
                        Matchers.contains(new NewDependency(DEPENDENCY_GROUP, DEPENDENCY_ID, DEPENDENCY_VERSION))),
                () -> assertThat(mavenProject.getProjectVersion(), equalTo(TestMavenModel.PROJECT_VERSION))//
        );
    }

    MavenProjectCrawlResult crawlProject(final Path... pomFiles) {
        return new JavaProjectCrawlerRunner(testMavenRepo, TestEnvBuilder.CURRENT_VERSION).crawlProject(pomFiles);
    }

    @Test
    void testGetDependencyChangesFailsForEmptyPomList() {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, this::crawlProject);
        assertThat(exception.getMessage(), containsString(
                "E-PK-MPC-64: Property 'projectsToCrawl' is not defined or empty. Specify property with least one pom file."));
    }

    @Test
    void testGetDependencyChangesFailsForMissingPom() {
        final Path missingPomFile = Path.of("missing-pom-file");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> this.crawlProject(missingPomFile));
        assertThat(exception.getMessage(),
                allOf(containsString("[FATAL] Non-readable POM"), containsString(missingPomFile.toString())));
    }

    private void writePomFile(final Path pomFile) {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency(DEPENDENCY_ID, DEPENDENCY_GROUP, "", DEPENDENCY_VERSION);
        new PomFileIO().writePom(model, pomFile);
    }
}
