package com.exasol.projectkeeper.validators.changesfile;

import java.io.IOException;
import java.nio.file.*;

class TemporaryPomFile implements AutoCloseable {
    private final Path pomFile;

    public TemporaryPomFile(final String content) throws IOException {
        this.pomFile = Files.createTempFile("pom-file-cache", ".xml");
        Files.writeString(this.pomFile, content, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void close() throws IOException {
        Files.delete(this.pomFile);
    }

    public Path getPomFile() {
        return this.pomFile;
    }
}
