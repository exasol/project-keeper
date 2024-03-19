package com.exasol.projectkeeper.dependencyupdate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.exasol.errorreporting.ExaError;

/**
 * This class provides methods to read and write text files.
 */
class TextFileIO {
    /**
     * Read a text file.
     * 
     * @param path path to the file
     * @return content of the file
     */
    String readTextFile(final Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("E-PK-CORE-192").message("Failed to read file {{path}}", path).toString(),
                    exception);
        }
    }

    /**
     * Write a text file.
     * 
     * @param path    path to the file
     * @param content content to write
     */
    void writeTextFile(final Path path, final String content) {
        try {
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("E-PK-CORE-193").message("Failed to write file {{path}}", path).toString(),
                    exception);
        }
    }
}
