package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.dependencies.ProjectDependency.Type.COMPILE;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.dependencies.*;
import com.exasol.projectkeeper.validators.TestMavenModel;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.DependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.dependencies.model.NewDependency;

class JavaProjectCrawlerRunnerTest {
    private static final String DEPENDENCY_ID = "error-reporting-java";
    private static final String DEPENDENCY_GROUP = "com.exasol";
    private static final String DEPENDENCY_VERSION = "0.4.1";

    @Test
    void testGetDependencyChanges(@TempDir Path tempDir) throws GitAPIException, IOException {
        Path pomFile = tempDir.resolve("pom.xml");
        Git.init().setDirectory(tempDir.toFile()).call().close();
        writePomFile(pomFile);
        final DependencyChangeReport report = new JavaProjectCrawlerRunner(pomFile).getDependencyChanges();
        assertThat(report.getCompileDependencyChanges(),
                Matchers.contains(new NewDependency(DEPENDENCY_GROUP, DEPENDENCY_ID, DEPENDENCY_VERSION)));
    }

    @Test
    void testGetDependencies(@TempDir Path tempDir) throws IOException {
        Path pomFile = tempDir.resolve("pom.xml");
        writePomFile(pomFile);
        final ProjectDependencies dependencies = new JavaProjectCrawlerRunner(pomFile).getDependencies();
        assertThat(dependencies.getDependencies(),
                Matchers.hasItem(
                        new ProjectDependency("error-reporting-java", "https://github.com/exasol/error-reporting-java",
                                List.of(new License("MIT", "https://opensource.org/licenses/MIT")), COMPILE)));
    }

    private void writePomFile(Path pomFile) throws IOException {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency(DEPENDENCY_ID, DEPENDENCY_GROUP, "", DEPENDENCY_VERSION);
        try (final FileWriter fileWriter = new FileWriter(pomFile.toFile())) {
            new MavenXpp3Writer().write(fileWriter, model);
        }
    }
}