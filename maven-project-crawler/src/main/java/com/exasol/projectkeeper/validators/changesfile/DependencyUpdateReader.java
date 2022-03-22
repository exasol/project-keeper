package com.exasol.projectkeeper.validators.changesfile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportReader;

/**
 * Calculate the dependencies updated since the last release.
 */
public class DependencyUpdateReader {
    private final MavenProjectFromFileReader mavenModelReader;
    private final Path projectDirectory;
    private final Model currentMavenModel;

    /**
     * Create a new instance of {@link DependencyUpdateReader}.
     * 
     * @param mavenModelReader  maven model reader
     * @param projectDirectory  project directory
     * @param currentMavenModel current project
     */
    public DependencyUpdateReader(final MavenProjectFromFileReader mavenModelReader, final Path projectDirectory,
            final Model currentMavenModel) {
        this.mavenModelReader = mavenModelReader;
        this.projectDirectory = projectDirectory;
        this.currentMavenModel = currentMavenModel;
    }

    /**
     * Read the dependency changes.
     * 
     * @return dependency changes report
     */
    public DependencyChangeReport readDependencyChanges() {
        return new DependencyChangeReportReader().read(getOldModel(), this.currentMavenModel);
    }

    private Model getOldModel() {
        final Path relativePathToPom = this.projectDirectory.relativize(this.currentMavenModel.getPomFile().toPath());
        final Path tempDirectory = createTempDirectory();
        try {
            final Optional<Path> lastReleasesPomFile = new LastReleasePomFileReader().extractLatestReleasesPomFile(
                    this.projectDirectory, relativePathToPom, this.currentMavenModel.getVersion(), tempDirectory);
            if (lastReleasesPomFile.isPresent()) {
                return parseOldPomFile(lastReleasesPomFile.get());
            } else {
                final var emptyModel = new Model();
                final var build = new Build();
                emptyModel.setBuild(build);
                return emptyModel;
            }
        } finally {
            deleteTempDir(tempDirectory);
        }
    }

    private void deleteTempDir(final Path tempDirectory) {
        try {
            if (Files.isDirectory(tempDirectory)) {
                try (final Stream<Path> contentStream = Files.list(tempDirectory)) {
                    contentStream.forEach(this::deleteTempDir);
                }
            }
            Files.delete(tempDirectory);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-63").message(
                    "Failed to delete the temp directory we created for buffering the pom file of the previous release.")
                    .toString(), exception);
        }
    }

    @SuppressWarnings("java:S5443") // using temp is ok here, since the data that is stored there is not confidential,
                                    // and we don't expect attackers on the developer's PCs.
    private Path createTempDirectory() {
        try {
            return Files.createTempDirectory("pk-pom-buffer");
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-62")
                    .message("Failed to create a temp directory for buffering the pom file of the previous release.")
                    .toString(), exception);
        }
    }

    private Model parseOldPomFile(final Path pomFile) {
        try {
            return this.mavenModelReader.readProject(pomFile.toFile()).getModel();
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-38")
                    .message("Failed to parse pom file of previous release.").toString(), exception);
        }
    }
}
