package com.exasol.projectkeeper.githuboutput;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest->dsn~verify-modes.output-parameters~1]
class FileOutputPublisherTest {

    @TempDir
    Path tempDir;

    @Test
    void directoryDoesNotExist() throws IOException {
        final Path file = tempDir.resolve("missingDir").resolve("file");
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class, () -> testee(file));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-188: Failed to open '"));
    }

    @Test
    @SuppressWarnings("try") // auto-closeable resource publisher is never referenced in body of corresponding try
                             // statement
    void fileDoesNotYetExistCreatesFile() throws IOException {
        final Path file = tempDir.resolve("file");
        try (OutputPublisher publisher = testee(file)) {
            // Don't write to file
        }
        assertThat(Files.size(file), is(0L));
    }

    @Test
    void writeSingleEntry() throws IOException {
        final Path file = tempDir.resolve("file");
        try (OutputPublisher publisher = testee(file)) {
            publisher.publish("key1", "value1");
        }
        assertThat(Files.readString(file), equalTo("key1=value1\n"));
    }

    @Test
    void writeMultipleEntries() throws IOException {
        final Path file = tempDir.resolve("file");
        try (OutputPublisher publisher = testee(file)) {
            publisher.publish("key1", "value1");
            publisher.publish("key2", "value2");
        }
        assertThat(Files.readString(file), equalTo("key1=value1\nkey2=value2\n"));
    }

    @Test
    void writeMultilineEntry() throws IOException {
        final Path file = tempDir.resolve("file");
        try (OutputPublisher publisher = testee(file)) {
            publisher.publish("key1", "value1\nmore");
            publisher.publish("key2", "value2");
        }
        assertThat(Files.readString(file), equalTo("key1<<EOF\nvalue1\nmore\nEOF\nkey2=value2\n"));
    }

    @Test
    void appendExistingFile() throws IOException {
        final Path file = tempDir.resolve("file");
        Files.writeString(file, "existing\ncontent\n");
        try (OutputPublisher publisher = testee(file)) {
            publisher.publish("key", "value");
        }
        assertThat(Files.readString(file), equalTo("existing\ncontent\nkey=value\n"));
    }

    OutputPublisher testee(final Path file) {
        return FileOutputPublisher.create(file);
    }
}
