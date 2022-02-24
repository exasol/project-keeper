package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.util.Optional;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.shared.model.DependencyChangeReport;
import com.exasol.projectkeeper.validators.changesfile.dependencies.DependencyChangeReportReader;

/**
 * Calculate the dependencies updated since the last release.
 */
class DependencyUpdateReader {
    private final MavenProjectFromFileReader mavenModelReader;
    private final Path projectDirectory;
    private final Model currentMavenModel;

    DependencyUpdateReader(final MavenProjectFromFileReader mavenModelReader, final Path projectDirectory,
            final Model currentMavenModel) {
        this.mavenModelReader = mavenModelReader;
        this.projectDirectory = projectDirectory;
        this.currentMavenModel = currentMavenModel;
    }

    DependencyChangeReport getDependencyUpdates() {
        return new DependencyChangeReportReader().read(getOldModel(), this.currentMavenModel);
    }

    private Model getOldModel() {
        final Optional<String> lastReleasesPomFile = new LastReleasePomFileReader()
                .readLatestReleasesPomFile(this.projectDirectory, this.currentMavenModel.getVersion());
        if (lastReleasesPomFile.isPresent()) {
            return parseOldPomFile(lastReleasesPomFile.get());
        } else {
            final var emptyModel = new Model();
            final var build = new Build();
            emptyModel.setBuild(build);
            return emptyModel;
        }
    }

    private Model parseOldPomFile(final String pomFileContents) {
        try (final var temporaryPomFile = new TemporaryPomFile(pomFileContents)) {
            return this.mavenModelReader.readProject(temporaryPomFile.getPomFile().toFile()).getModel();
        } catch (final MavenProjectFromFileReader.ReadFailedException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-MPC-38")
                    .message("Failed to parse pom file of previous release.").toString(), exception);
        }
    }
}
