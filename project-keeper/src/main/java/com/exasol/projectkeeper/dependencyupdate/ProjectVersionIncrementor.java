package com.exasol.projectkeeper.dependencyupdate;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.JAR_ARTIFACT;

import java.nio.file.Path;
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.analyze.generic.*;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;
import com.vdurmont.semver4j.Semver;

/**
 * This class can increment the project's version.
 */
// [impl->dsn~dependency-updater.increment-version~1]
class ProjectVersionIncrementor {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private final ProjectKeeperConfig config;
    private final String currentProjectVersion;
    private final ChangesFileIO changesFileIO;
    private final Clock clock;
    private final Path projectDir;
    private final Logger logger;
    private final TextFileIO textFileIO;
    private final CommandExecutor commandExecutor;

    ProjectVersionIncrementor(final ProjectKeeperConfig config, final Logger logger, final Path projectDir,
            final String currentProjectVersion) {
        this(config, logger, projectDir, currentProjectVersion, new ChangesFileIO(), new TextFileIO(),
                new CommandExecutor(), Clock.systemUTC());
    }

    ProjectVersionIncrementor(final ProjectKeeperConfig config, final Logger logger, final Path projectDir,
            final String currentProjectVersion, final ChangesFileIO changesFileIO, final TextFileIO textFileIO,
            final CommandExecutor commandExecutor, final Clock clock) {
        this.config = config;
        this.logger = logger;
        this.projectDir = projectDir;
        this.changesFileIO = changesFileIO;
        this.textFileIO = textFileIO;
        this.commandExecutor = commandExecutor;
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

    /**
     * Increment the project's patch version. If the project produces a JAR (i.e. uses the {@code JAR_ARTIFACT} module),
     * it runs its {@code artifact-reference-checker:unify} goal to update references to the JAR file.
     * 
     * @return the new, incremented version
     */
    String incrementProjectVersion() {
        final String nextVersion = getIncrementedVersion(currentProjectVersion);
        incrementVersionInPom(nextVersion);
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
        final ShellCommand command = MavenProcessBuilder.create().addArgument("artifact-reference-checker:unify")
                .workingDir(projectDir).timeout(Duration.ofSeconds(30)).buildCommand();
        commandExecutor.execute(command);
    }

    private String incrementVersionInPom(final String nextVersion) {
        final Path path = getPomPath();
        final String pomContent = textFileIO.readTextFile(path);
        logger.info("Incrementing version from " + currentProjectVersion + " to " + nextVersion + " in POM " + path);
        textFileIO.writeTextFile(path, updateVersionInPom(path, pomContent, nextVersion));
        return nextVersion;
    }

    private String updateVersionInPom(final Path path, final String pomContent, final String nextVersion) {
        String updatedPom = pomContent;
        updatedPom = replaceTagContent(updatedPom, "version", currentProjectVersion, nextVersion);
        updatedPom = replaceTagContent(updatedPom, "revision", currentProjectVersion, nextVersion);
        if (updatedPom.equals(pomContent)) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-194")
                    .message("Failed to update version in POM {{path}}. No version tag found.", path).toString());
        }
        return updatedPom;
    }

    private static String replaceTagContent(final String content, final String tagName, final String oldTagContent,
            final String newTagContent) {
        final String startTag = "<" + tagName + ">";
        final String endTag = "</" + tagName + ">";
        final String oldContent = startTag + oldTagContent + endTag;
        final String newContent = startTag + newTagContent + endTag;
        return content.replaceFirst(Pattern.quote(oldContent), newContent);
    }

    static String getIncrementedVersion(final String version) {
        final Semver current = new Semver(version);
        return current.nextPatch().toString();
    }

    private Path getPomPath() {
        return projectDir.resolve("pom.xml");
    }
}
