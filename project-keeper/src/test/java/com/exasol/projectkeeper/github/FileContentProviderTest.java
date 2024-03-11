package com.exasol.projectkeeper.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// [utest->dsn~verify-modes.output-parameters~1]
@ExtendWith(MockitoExtension.class)
class FileContentProviderTest {

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
        try (WorkflowOutput publisher = testee(file)) {
            // Don't write to file
        }
        assertThat(Files.size(file), is(0L));
    }

    @Test
    void writeSingleEntry() throws IOException {
        final Path file = tempDir.resolve("file");
        try (WorkflowOutput publisher = testee(file)) {
            publisher.publish("key1", "value1");
        }
        assertThat(Files.readString(file), equalTo("key1=value1\n"));
    }

    @Test
    void writeMultipleEntries() throws IOException {
        final Path file = tempDir.resolve("file");
        try (WorkflowOutput publisher = testee(file)) {
            publisher.publish("key1", "value1");
            publisher.publish("key2", "value2");
        }
        assertThat(Files.readString(file), equalTo("key1=value1\nkey2=value2\n"));
    }

    @Test
    void writeMultilineEntry() throws IOException {
        final Path file = tempDir.resolve("file");
        try (WorkflowOutput publisher = testee(file)) {
            publisher.publish("key1", "value1\nmore");
            publisher.publish("key2", "value2");
        }
        assertThat(Files.readString(file), equalTo("key1<<EOF\nvalue1\nmore\nEOF\nkey2=value2\n"));
    }

    @Test
    void appendExistingFile() throws IOException {
        final Path file = tempDir.resolve("file");
        Files.writeString(file, "existing\ncontent\n");
        try (WorkflowOutput publisher = testee(file)) {
            publisher.publish("key", "value");
        }
        assertThat(Files.readString(file), equalTo("existing\ncontent\nkey=value\n"));
    }

    @Test
    @SuppressWarnings("resource") // AutoClosable not closed by intention
    void writeFails(@Mock final Writer writerMock) throws IOException {
        final FileContentProvider publisher = new FileContentProvider(writerMock, Path.of("file"));
        doThrow(new IOException("expected")).when(writerMock).write(ArgumentMatchers.any(String.class));
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class,
                () -> publisher.publish("key", "value"));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-189: Failed to write content 'key=value\n' to file 'file': 'expected'"));
    }

    @Test
    void closeFails(@Mock final Writer writerMock) throws IOException {
        final FileContentProvider publisher = new FileContentProvider(writerMock, Path.of("file"));
        doThrow(new IOException("expected")).when(writerMock).close();
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class, publisher::close);
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-187: Failed to close file 'file' after writing: 'expected'"));
    }

    WorkflowOutput testee(final Path file) {
        return FileContentProvider.create(file);
    }
}
