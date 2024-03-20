package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;
import java.time.Duration;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeper;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.analyze.generic.*;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

/**
 * This class runs the dependency update process.
 */
public class DependencyUpdater {

    private static final Duration MAVEN_COMMAND_TIMEOUT = Duration.ofSeconds(60);
    private final ProjectKeeper projectKeeper;
    private final Logger logger;
    private final Path projectDir;
    private final ProjectVersionIncrementor projectVersionIncrementor;
    private final String currentProjectVersion;
    private final ChangesFileUpdater changesFileUpdater;
    private final CommandExecutor commandExecutor;

    DependencyUpdater(final ProjectKeeper projectKeeper, final Logger logger, final Path projectDir,
            final String currentProjectVersion, final ProjectVersionIncrementor projectVersionIncrementor,
            final ChangesFileUpdater changesFileUpdater, final CommandExecutor commandExecutor) {
        this.projectKeeper = projectKeeper;
        this.logger = logger;
        this.projectDir = projectDir;
        this.currentProjectVersion = currentProjectVersion;
        this.projectVersionIncrementor = projectVersionIncrementor;
        this.changesFileUpdater = changesFileUpdater;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Create a new instance.
     * 
     * @param projectKeeper         project keeper reference
     * @param config                project keeper configuration
     * @param logger                the logger to which we should write log messages
     * @param projectDir            the project directory
     * @param currentProjectVersion the project's current version
     * @return a new dependency updater
     */
    public static DependencyUpdater create(final ProjectKeeper projectKeeper, final ProjectKeeperConfig config,
            final Logger logger, final Path projectDir, final String currentProjectVersion) {
        return new DependencyUpdater(projectKeeper, logger, projectDir, currentProjectVersion,
                new ProjectVersionIncrementor(config, logger, projectDir, currentProjectVersion),
                new ChangesFileUpdater(new VulnerabilityInfoProvider(), new ChangesFileIO(), projectDir),
                new CommandExecutor());
    }

    /**
     * Runs the dependency update process. This includes the following steps:
     * <ol>
     * <li>Increment project patch version if necessary</li>
     * <li>Update all dependencies to their latest versions</li>
     * <li>Run project-keeper fix</li>
     * <li>If available: add information about fixed vulnerabilities to changelog</li>
     * </ol>
     * 
     * @return {@code true} if the process succeeded.
     */
    // [impl->dsn~dependency-updater.increment-version~2]
    // [impl->dsn~dependency-updater.update-changelog~1]
    public boolean updateDependencies() {
        final String version = incrementProjectVersion();
        updateDependencyVersions();
        runProjectKeeperFix();
        changesFileUpdater.updateChanges(version);
        return true;
    }

    private String incrementProjectVersion() {
        if (projectVersionIncrementor.isCurrentVersionReleased()) {
            logger.info("Current version was already released: increment version");
            return projectVersionIncrementor.incrementProjectVersion();
        } else {
            logger.info("Current version was not yet released: no need to increment");
            return currentProjectVersion;
        }
    }

    // [impl->dsn~dependency-updater.update-dependencies~1]
    private void updateDependencyVersions() {
        runMaven("versions:use-latest-releases");
        runMaven("versions:update-properties");
    }

    private void runMaven(final String mavenGoal) {
        final ShellCommand command = MavenProcessBuilder.create().addArgument(mavenGoal).workingDir(projectDir)
                .timeout(MAVEN_COMMAND_TIMEOUT).buildCommand();
        commandExecutor.execute(command);
    }

    private void runProjectKeeperFix() {
        logger.info("Running project-keeper fix...");
        if (!projectKeeper.fix()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-177")
                    .message("Running project-keeper fix failed, see errors above.")
                    .mitigation("Fix findings and try again.").toString());
        }
    }
}
