package com.exasol.projectkeeper.validators.changelog;

import static com.exasol.projectkeeper.validators.changelog.ChangelogFile.DEPENDENCY_UPDATES_HEADLINE;
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

class DependencySectionFixerTest {

    @TempDir
    static Path tempDir;

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
        final ChangelogFile changelogFile = ChangelogFile.builder().setHeader(List.of("headline")).build();
        final List<ChangelogSection> sections = new DependencySectionFixer(tempDir).fix(changelogFile).getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeadline(), equalTo(DEPENDENCY_UPDATES_HEADLINE));
    }

    @Test
    void testSectionIsUpdated() {
        final ChangelogFile changelogFile = ChangelogFile.builder().setHeader(List.of("headline"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADLINE, "myLine")).build();
        final ChangelogFile fixedChangelogFile = new DependencySectionFixer(tempDir).fix(changelogFile);
        final List<ChangelogSection> sections = fixedChangelogFile.getSections();
        assertThat(sections.size(), equalTo(1));
        assertThat(sections.get(0).getHeadline(), equalTo(DEPENDENCY_UPDATES_HEADLINE));
        assertThat("dependency fixer changed the changelog", changelogFile, not(equalTo(fixedChangelogFile)));
    }

    @Test
    void testHeaderIsPreserved() {
        final ChangelogFile changelogFile = ChangelogFile.builder().setHeader(List.of("headline"))
                .addSection(List.of(DEPENDENCY_UPDATES_HEADLINE, "myLine")).build();
        final ChangelogFile fixedChangelogFile = new DependencySectionFixer(tempDir).fix(changelogFile);
        assertThat(changelogFile.getHeaderLines(), equalTo(fixedChangelogFile.getHeaderLines()));
    }
}