package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;

import com.exasol.projectkeeper.Logger;

/**
 * This class runs the dependency update process.
 */
public class DependencyUpdater {

    private final Logger logger;
    private final ProjectVersionIncrementor projectVersionIncrementor;

    DependencyUpdater(final Logger logger, final ProjectVersionIncrementor projectVersionIncrementor) {
        this.logger = logger;
        this.projectVersionIncrementor = projectVersionIncrementor;

    }

    public static DependencyUpdater create(final Logger logger, final Path projectDir,
            final String currentProjectVersion) {
        return new DependencyUpdater(logger, new ProjectVersionIncrementor(logger, projectDir, currentProjectVersion));
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
        // TODO Auto-generated method stub
    }

    private void runProjectKeeperFix() {
        // TODO Auto-generated method stub
    }

    private void updateChangelog() {
        // TODO Auto-generated method stub
    }
}
