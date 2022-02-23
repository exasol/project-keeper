package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenpluginintegrationtesting.MavenIntegrationTestEnvironment;
import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.shared.model.DependencyChangeReport;
import com.exasol.projectkeeper.shared.model.NewDependency;
import com.exasol.projectkeeper.validators.TestMavenModel;

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
        final Path pomFile = tempDir.resolve("pom.xml");
        Git.init().setDirectory(tempDir.toFile()).call().close();
        writePomFile(pomFile);
        final DependencyChangeReport report = new JavaProjectCrawlerRunner(testMavenRepo,
                TestEnvBuilder.CURRENT_VERSION).getDependencyChanges(pomFile);
        assertThat(report.getCompileDependencyChanges(),
                Matchers.contains(new NewDependency(DEPENDENCY_GROUP, DEPENDENCY_ID, DEPENDENCY_VERSION)));
    }

    @Test
    void testGetDependencies(@TempDir final Path tempDir) throws IOException {
        final Path pomFile = tempDir.resolve("pom.xml");
        writePomFile(pomFile);
        final ProjectDependencies dependencies = new JavaProjectCrawlerRunner(testMavenRepo,
                TestEnvBuilder.CURRENT_VERSION).getDependencies(pomFile);
        assertThat(dependencies.getDependencies(),
                Matchers.hasItem(
                        new ProjectDependency("error-reporting-java", "https://github.com/exasol/error-reporting-java",
                                List.of(new License("MIT", "https://opensource.org/licenses/MIT")), COMPILE)));
    }

    private void writePomFile(final Path pomFile) throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency(DEPENDENCY_ID, DEPENDENCY_GROUP, "", DEPENDENCY_VERSION);
        try (final FileWriter fileWriter = new FileWriter(pomFile.toFile())) {
            new MavenXpp3Writer().write(fileWriter, model);
        }
    }
}