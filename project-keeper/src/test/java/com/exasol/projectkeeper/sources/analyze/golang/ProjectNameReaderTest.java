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
    void withoutGitConfig(@TempDir final Path tempDir) throws IOException {
        emulateGitProject(tempDir, "");
        assertThat(RepoNameReader.getRepoName(tempDir), equalTo(tempDir.getFileName().toString()));
    }

    @Test
    void gitConfig(@TempDir final Path tempDir) throws IOException {
        emulateGitProject(tempDir, "[remote \"origin\"]\n" //
                + " url = https://github.com/exasol/remote-repo.git\n");
        assertThat(RepoNameReader.getRepoName(tempDir), equalTo("remote-repo"));
    }

    private void emulateGitProject(final Path folder, final String configuration) throws IOException {
        Files.createDirectories(folder.resolve(".git/refs"));
        Files.createDirectories(folder.resolve(".git/objects"));
        Files.writeString(folder.resolve(".git/HEAD"), "ref: refs/heads/refactor/380-get-project-name");
        final Path configFile = folder.resolve(".git/config");
        Files.writeString(configFile, configuration);
    }
}
