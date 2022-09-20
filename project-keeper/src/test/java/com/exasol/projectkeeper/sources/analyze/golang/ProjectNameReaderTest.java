package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProjectNameReaderTest {

    @Test
    void withoutGitConfig(@TempDir final Path tempDir) {
        assertThat(ProjectNameReader.getProjectName(tempDir), equalTo(tempDir.getFileName().toString()));
    }

    @Test
    void gitConfig(@TempDir final Path tempDir) throws IOException {
        final Path file = tempDir.resolve(ProjectNameReader.GIT_CONFIG);
        Files.createDirectories(file.getParent());
        Files.writeString(file, " url = https://github.com/exasol/remote-repo.git");
        assertThat(ProjectNameReader.getProjectName(tempDir), equalTo("remote-repo"));
    }
}
