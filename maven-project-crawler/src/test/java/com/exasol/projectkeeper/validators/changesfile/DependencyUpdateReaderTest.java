package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.TestMavenModel;
import com.exasol.projectkeeper.TestRepo;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.shared.dependencychanges.UpdatedDependency;

class DependencyUpdateReaderTest {

    private static final String EXAMPLE_GROUP_ID = "com.example";
    private static final String EXAMPLE_ARTIFACT_ID = "my-lib";
    @TempDir
    Path tempDir;

    @Test
    void testReadUpdates() throws GitAPIException, IOException {
        try (final TestRepo gitRepo = new TestRepo(this.tempDir)) {
            makeRelease(gitRepo, "1.0.0", "1.2.3");
            makeRelease(gitRepo, null, "1.2.4");
            final MavenProject currentProject = new MavenProjectFromFileReaderStub()
                    .readProject(this.tempDir.resolve("pom.xml").toFile());
            final DependencyChangeReport result = new DependencyUpdateReader(new MavenProjectFromFileReaderStub(),
                    this.tempDir, currentProject.getModel()).readDependencyChanges();
            assertThat(result.getCompileDependencyChanges(),
                    hasItem(new UpdatedDependency(EXAMPLE_GROUP_ID, EXAMPLE_ARTIFACT_ID, "1.2.3", "1.2.4")));
        }
    }

    private String makeRelease(final TestRepo git, final String name, final String dependencyVersion)
            throws IOException, GitAPIException {
        final TestMavenModel testMavenModel = new TestMavenModel();
        testMavenModel.addDependency(EXAMPLE_ARTIFACT_ID, EXAMPLE_GROUP_ID, "compile", dependencyVersion);
        testMavenModel.writeAsPomToProject(this.tempDir);
        git.addAll().commit();
        if (name != null) {
            git.createTag(name);
        }
        return Files.readString(this.tempDir.resolve("pom.xml"));
    }

    private static class MavenProjectFromFileReaderStub implements MavenProjectFromFileReader {

        @Override
        public MavenProject readProject(final File pomFile) {
            try (final FileReader fileReader = new FileReader(pomFile)) {
                final Model model = new MavenXpp3Reader().read(fileReader);
                model.setPomFile(pomFile);
                return new MavenProject(model);
            } catch (final XmlPullParserException | IOException exception) {
                throw new IllegalStateException(exception);
            }
        }
    }
}