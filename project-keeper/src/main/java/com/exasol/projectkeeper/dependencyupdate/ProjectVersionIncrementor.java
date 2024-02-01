package com.exasol.projectkeeper.dependencyupdate;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.JAR_ARTIFACT;

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
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.analyze.generic.MavenProcessBuilder;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;
import com.vdurmont.semver4j.Semver;

class ProjectVersionIncrementor {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private final ProjectKeeperConfig config;
    private final String currentProjectVersion;
    private final ChangesFileIO changesFileIO;
    private final Clock clock;
    private final Path projectDir;
    private final Logger logger;

    ProjectVersionIncrementor(final ProjectKeeperConfig config, final Logger logger, final Path projectDir,
            final String currentProjectVersion) {
        this(config, logger, projectDir, currentProjectVersion, new ChangesFileIO(), Clock.systemUTC());
    }

    ProjectVersionIncrementor(final ProjectKeeperConfig config, final Logger logger, final Path projectDir,
            final String currentProjectVersion, final ChangesFileIO changesFileIO, final Clock clock) {
        this.config = config;
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

    String incrementProjectVersion() {
        final Model pom = readPom();
        if (!this.currentProjectVersion.equals(pom.getVersion())) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-174").message(
                    "Inconsistent project version {{version in pom file}} found in pom {{pom file path}}, expected {{expected version}}",
                    pom.getVersion(), pom.getPomFile(), currentProjectVersion).ticketMitigation().toString());
        }
        final String nextVersion = incrementVersion(pom);
        if (usesReferenceCheckerPlugin()) {
            updateReferences();
        }
        return nextVersion;
    }

    private boolean usesReferenceCheckerPlugin() {
        return config.getSources().stream().anyMatch(source -> source.getModules().contains(JAR_ARTIFACT));
    }

    private void updateReferences() {
        logger.info("Unify artifact references");
        MavenProcessBuilder.create().addArgument("artifact-reference-checker:unify").workingDir(projectDir)
                .startSimpleProcess().waitUntilFinished(Duration.ofSeconds(30));
    }

    private String incrementVersion(final Model pom) {
        final String nextVersion = getIncrementedVersion(currentProjectVersion);
        logger.info("Incrementing version from " + currentProjectVersion + " to " + nextVersion + " in POM "
                + pom.getPomFile());
        pom.setVersion(nextVersion);
        writePom(pom);
        return nextVersion;
    }

    static String getIncrementedVersion(final String version) {
        final Semver current = new Semver(version);
        return current.nextPatch().toString();
    }

    private Model readPom() {
        final Path path = getPomPath();
        try {
            return new MavenXpp3Reader().read(Files.newBufferedReader(path));
        } catch (IOException | XmlPullParserException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-172")
                    .message("Failed to read pom {{pom file path}}", path).toString(), exception);
        }
    }

    private void writePom(final Model pom) {
        final Path path = getPomPath();
        try {
            new MavenXpp3Writer().write(Files.newOutputStream(path), pom);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-173")
                    .message("Failed to write pom {{pom file path}}", path).toString(), exception);
        }
    }

    private Path getPomPath() {
        return projectDir.resolve("pom.xml");
    }
}
