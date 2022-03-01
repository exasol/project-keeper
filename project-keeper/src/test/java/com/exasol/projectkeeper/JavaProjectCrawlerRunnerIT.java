package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.shared.dependencies.License;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.NewDependency;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.CrawledMavenProject;
import com.exasol.projectkeeper.shared.mavenprojectcrawler.MavenProjectCrawlResult;
import com.exasol.projectkeeper.validators.TestMavenModel;

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
    void testGetDependencyChanges(@TempDir final Path tempDir) throws GitAPIException, IOException {
        Git.init().setDirectory(tempDir.toFile()).call().close();
        final Path pomFile = tempDir.resolve("pom.xml");
        writePomFile(pomFile);
        final MavenProjectCrawlResult result = new JavaProjectCrawlerRunner(testMavenRepo,
                TestEnvBuilder.CURRENT_VERSION).crawlProject(pomFile);
        final CrawledMavenProject mavenProject = result.getCrawledProjects()
                .get(pomFile.toAbsolutePath().toString().replace("\\", "/"));
        assertAll(//
                () -> assertThat(mavenProject.getProjectDependencies().getDependencies(),
                        Matchers.hasItem(new ProjectDependency("error-reporting-java",
                                "https://github.com/exasol/error-reporting-java",
                                List.of(new License("MIT", "https://opensource.org/licenses/MIT")), COMPILE))),
                () -> assertThat(mavenProject.getDependencyChangeReport().getCompileDependencyChanges(),
                        Matchers.contains(new NewDependency(DEPENDENCY_GROUP, DEPENDENCY_ID, DEPENDENCY_VERSION))),
                () -> assertThat(mavenProject.getProjectVersion(), equalTo(TestMavenModel.PROJECT_VERSION))//
        );
    }

    private void writePomFile(final Path pomFile) throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency(DEPENDENCY_ID, DEPENDENCY_GROUP, "", DEPENDENCY_VERSION);
        try (final FileWriter fileWriter = new FileWriter(pomFile.toFile())) {
            new MavenXpp3Writer().write(fileWriter, model);
        }
    }
}