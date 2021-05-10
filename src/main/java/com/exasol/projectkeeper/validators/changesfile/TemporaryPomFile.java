package com.exasol.projectkeeper.validators.changesfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.exasol.errorreporting.ExaError;

class TemporaryPomFile implements AutoCloseable {
    private final Path pomFile;

    public TemporaryPomFile(final String content) throws IOException {
        this.pomFile = Files.createTempFile("pom-file-cache", ".xml");
        restrictFileAccess(this.pomFile.toFile());
        Files.writeString(this.pomFile, content, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void restrictFileAccess(final File file) {
        if (!(file.setReadable(false, false)//
                && file.setWritable(false, false)//
                && file.setExecutable(false, false))) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-56")
                    .message("Failed to restrict permissions for temporary file {{file}}", file).toString());
        }
        if (!(file.setReadable(true, true)//
                && file.setWritable(true, true))) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-57")
                    .message("Failed to grant owner permissions for temporary file {{file}}", file).toString());
        }
    }

    @Override
    public void close() throws IOException {
        Files.delete(this.pomFile);
    }

    public Path getPomFile() {
        return this.pomFile;
    }
}
