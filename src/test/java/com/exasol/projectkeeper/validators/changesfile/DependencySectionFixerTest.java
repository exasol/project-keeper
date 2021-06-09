package com.exasol.projectkeeper.validators.changesfile;

import static com.exasol.projectkeeper.validators.changesfile.ChangesFile.DEPENDENCY_UPDATES_HEADING;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.validators.SimpleMavenProjectFromFileReader;
import com.exasol.projectkeeper.validators.TestMavenModel;

class DependencySectionFixerTest {
    @TempDir
    static Path tempDir;
    private static final MavenProjectFromFileReader MAVEN_MODEL_READER = new SimpleMavenProjectFromFileReader();

    @BeforeAll
    static void beforeAll() throws GitAPIException, IOException {
        Git.init().setDirectory(tempDir.toFile()).call().close();
        try (final FileWriter fileWriter = new FileWriter(tempDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, createModelWithDependency());
        }
    }

    private static Model createModelWithDependency() {
        final TestMavenModel model = new TestMavenModel();
        model.addDependency();
        return model;
    }

    @Test
    void testSectionIsAdded() {
        final ChangesFile changesFile = ChangesFile.builder().setHeader(List.of("heading")).build();
        final List<ChangesFileSection> sections = new DependencySectionFixer(MAVEN_MODEL_READER, tempDir)
                .fix(changesFile).getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeading(), equalTo(DEPENDENCY_UPDATES_HEADING));
    }

    @Test
    void testSectionIsUpdated() {
        final ChangesFile changesFile = ChangesFile.builder().setHeader(List.of("heading"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADING, "myLine")).build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(MAVEN_MODEL_READER, tempDir).fix(changesFile);
        final List<ChangesFileSection> sections = fixedChangesFile.getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeading(), equalTo(DEPENDENCY_UPDATES_HEADING));
        assertThat("dependency fixer changed the changes file", changesFile, not(equalTo(fixedChangesFile)));
    }

    @Test
    void testHeaderIsPreserved() {
        final ChangesFile changesFile = ChangesFile.builder().setHeader(List.of("heading"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADING, "myLine")).build();
        final ChangesFile fixedChangesFile = new DependencySectionFixer(MAVEN_MODEL_READER, tempDir).fix(changesFile);
        assertThat(changesFile.getHeaderSectionLines(), equalTo(fixedChangesFile.getHeaderSectionLines()));
    }
}