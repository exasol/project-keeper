package com.exasol.projectkeeper.validators.changesfile;

import java.io.IOException;
import java.nio.file.*;

import com.exasol.errorreporting.ExaError;

/**
 * This class temporarily saves a pom file so that it can be parsed by the
 * {@link org.apache.maven.project.MavenProjectBuildingResult} that unfortunately only can parse files.
 */
class TemporaryPomFile implements AutoCloseable {
    private final Path pomFile;

    /**
     * Create a new {@link TemporaryPomFile}.
     * 
     * @param content pom file content
     */
    @SuppressWarnings("java:S5443") // not attack-able via file manipulation in tmp since attacker must be on same
                                    // machine
    public TemporaryPomFile(final String content) {
        try {
            this.pomFile = Files.createTempFile("pom-file-cache", ".xml");
            Files.writeString(this.pomFile, content, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-58").message("Failed to temporarily store pom file on disk.")
                            .mitigation("Check the permissions for the temp directory of your OS.").toString(),
                    exception);
        }
    }

    @Override
    public void close() {
        try {
            Files.delete(this.pomFile);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-59").message("Failed to remove temporary cache file.")
                            .mitigation("Check the file permissions.").toString(),
                    exception);
        }
    }

    /**
     * Get the path of the pom file on disk.
     * 
     * @return path of the pom file
     */
    public Path getPomFile() {
        return this.pomFile;
    }
}
