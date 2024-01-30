package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeper;
import com.exasol.projectkeeper.sources.analyze.generic.MavenProcessBuilder;
import com.exasol.projectkeeper.sources.analyze.generic.SimpleProcess;

/**
 * This class runs the dependency update process.
 */
public class DependencyUpdater {

    private static final Duration MAVEN_COMMAND_TIMEOUT = Duration.ofSeconds(60);
    private final ProjectKeeper projectKeeper;
    private final Logger logger;
    private final Path projectDir;
    private final ProjectVersionIncrementor projectVersionIncrementor;

    DependencyUpdater(final ProjectKeeper projectKeeper, final Logger logger, final Path projectDir,
            final ProjectVersionIncrementor projectVersionIncrementor) {
        this.projectKeeper = projectKeeper;
        this.logger = logger;
        this.projectDir = projectDir;
        this.projectVersionIncrementor = projectVersionIncrementor;
    }

    /**
     * Create a new instance.
     * 
     * @param projectKeeper
     * 
     * @param projectKeeper         project keeper reference
     * @param logger                the logger to which we should write log messages
     * @param projectDir            the project directory
     * @param currentProjectVersion the project's current version
     * @return a new dependency updater
     */
    public static DependencyUpdater create(final ProjectKeeper projectKeeper, final Logger logger,
            final Path projectDir, final String currentProjectVersion) {
        return new DependencyUpdater(projectKeeper, logger, projectDir,
                new ProjectVersionIncrementor(logger, projectDir, currentProjectVersion));
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
    public boolean updateDependencies() {
        incrementProjectVersion();
        updateDependencyVersions();
        runProjectKeeperFix();
        updateChangelog();
        return true;
    }

    private void incrementProjectVersion() {
        if (projectVersionIncrementor.isCurrentVersionReleased()) {
            logger.info("Current version was already released: increment version");
            projectVersionIncrementor.incrementProjectVersion();
        } else {
            logger.info("Current version was not yet released: no need to increment");
        }
    }

    private void updateDependencyVersions() {
        runMaven("versions:use-latest-releases");
        runMaven("versions:update-properties");
    }

    private void runMaven(final String mavenGoal) {
        final List<String> command = MavenProcessBuilder.create().addArgument("--batch-mode").addArgument(mavenGoal)
                .build();
        final Instant start = Instant.now();
        SimpleProcess.start(projectDir, command).waitUntilFinished(MAVEN_COMMAND_TIMEOUT);
        logger.info("Running maven goal " + mavenGoal + " took " + Duration.between(start, Instant.now()));
    }

    private void runProjectKeeperFix() {
        logger.info("Running project-keeper fix");
        if (!projectKeeper.fix()) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-177")
                    .message("Running project-keeper fix failed, see errors above.")
                    .mitigation("Fix findings and try again.").toString());
        }
    }

    private void updateChangelog() {
        // TODO Auto-generated method stub
    }
}
