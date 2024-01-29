package com.exasol.projectkeeper.dependencyupdate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.Objects;
import java.util.Optional;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;
import com.vdurmont.semver4j.Semver;

class ProjectVersionIncrementor {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private final String currentProjectVersion;
    private final ChangesFileIO changesFileIO;
    private final Clock clock;
    private final Path projectDir;
    private final Logger logger;

    ProjectVersionIncrementor(final Logger logger, final Path projectDir, final String currentProjectVersion) {
        this(logger, projectDir, currentProjectVersion, new ChangesFileIO(), Clock.systemUTC());
    }

    ProjectVersionIncrementor(final Logger logger, final Path projectDir, final String currentProjectVersion,
            final ChangesFileIO changesFileIO, final Clock clock) {
        this.logger = logger;
        this.projectDir = projectDir;
        this.changesFileIO = changesFileIO;
        this.clock = clock;
        this.currentProjectVersion = Objects.requireNonNull(currentProjectVersion, "currentProjectVersion");
    }

    /**
     * Check if the current version was released, i.e. has a changelog release date in the past or today.
     * 
     * @return {@code true} if the current version was released (i.e. has a release date) or not (i.e. has no release
     *         date or a future date)
     */
    boolean isCurrentVersionReleased() {
        final Path changesFilePath = projectDir.resolve(ChangesFile.getPathForVersion(currentProjectVersion));
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        final Optional<LocalDate> releaseDate = changesFile.getParsedReleaseDate();
        if (releaseDate.isEmpty()) {
            logger.info("Found invalid date '" + changesFile.getReleaseDate() + "' in changelog " + changesFilePath
                    + ": version " + currentProjectVersion + " was not yet released");
            return false;
        }
        final boolean released = releaseDate.get().isBefore(today());
        if (released) {
            logger.info("Version " + currentProjectVersion + " was released on " + changesFile.getReleaseDate()
                    + " according to " + changesFilePath);
        }
        return released;
    }

    private LocalDate today() {
        return LocalDate.ofInstant(clock.instant(), UTC_ZONE);
    }

    void incrementProjectVersion() {
        final Path path = getPomPath();
        final Model pom = readPom(path);
        if (!this.currentProjectVersion.equals(pom.getVersion())) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-174").message(
                    "Inconsistent project version {{version in pom file}} found in pom {{pom file path}}, expected {{expected version}}",
                    pom.getVersion(), path, currentProjectVersion).toString());
        }
        final String nextVersion = getIncrementedVersion(currentProjectVersion);
        System.out.println("#### Incremeing  to " + nextVersion);
        logger.info("Incrementing version from " + currentProjectVersion + " to " + nextVersion + " in POM " + path);
        pom.setVersion(nextVersion);
        writePom(path, pom);
    }

    static String getIncrementedVersion(final String version) {
        final Semver current = new Semver(version);
        return current.nextPatch().toString();
    }

    private Model readPom(final Path path) {
        try {
            return new MavenXpp3Reader().read(Files.newBufferedReader(path));
        } catch (IOException | XmlPullParserException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-172")
                    .message("Failed to read pom {{pom file path}}", path).toString(), exception);
        }
    }

    private void writePom(final Path path, final Model pom) {
        try {
            new MavenXpp3Writer().write(Files.newOutputStream(getPomPath()), pom);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-173")
                    .message("Failed to write pom {{pom file path}}", path).toString(), exception);
        }
    }

    private Path getPomPath() {
        return projectDir.resolve("pom.xml");
    }
}
