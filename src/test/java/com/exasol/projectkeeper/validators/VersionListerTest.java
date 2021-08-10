package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class VersionListerTest {
    @Test
    void testListVersions(@TempDir final Path tempDir) throws IOException {
        final Path changesDirectory = tempDir.resolve(Path.of("doc", "changes"));
        Files.createDirectories(changesDirectory);
        Files.createFile(changesDirectory.resolve("changes_1.0.0.md"));
        Files.createFile(changesDirectory.resolve("changes_1.0.1.md"));
        assertThat(new VersionLister(tempDir).listVersions(), Matchers.containsInAnyOrder("1.0.0", "1.0.1"));
    }
}