package com.exasol.projectkeeper.dependencyupdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TextFileIOTest {

    @TempDir
    Path tempDir;

    @Test
    void readFileDoesNotExist() {
        final TextFileIO testee = testee();
        final Path path = tempDir.resolve("non-existing-file");
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class,
                () -> testee.readTextFile(path));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-192: Failed to read file '" + path + "'"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "test", "test\n", "test\n\n", "öäüÖÄÜß", "test\ntest\ntest\n", })
    void readFile(final String fileContent) {
        final TextFileIO testee = testee();
        final Path path = tempDir.resolve("empty-file");
        testee.writeTextFile(path, fileContent);
        assertThat(testee.readTextFile(path), equalTo(fileContent));
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "test", "test\n", "test\n\n", "öäüÖÄÜß", "test\ntest\ntest\n", })
    void writeFileNonExisting(final String fileContent) {
        final TextFileIO testee = testee();
        final Path path = tempDir.resolve("empty-file");
        testee.writeTextFile(path, fileContent);
        assertThat(testee.readTextFile(path), equalTo(fileContent));
    }

    @Test
    void writeFileOverwritesExistingFile() throws IOException {
        final TextFileIO testee = testee();
        final Path path = tempDir.resolve("existing-file");
        Files.writeString(path, "old content");
        testee.writeTextFile(path, "new content");
        assertThat(testee.readTextFile(path), equalTo("new content"));
    }

    @Test
    void writeFileFails() {
        final TextFileIO testee = testee();
        final Path path = tempDir.resolve("missing-dir/file");
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class,
                () -> testee.writeTextFile(path, "new content"));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-193: Failed to write file '" + path + "'"));
    }

    private TextFileIO testee() {
        return new TextFileIO();
    }
}
