package com.exasol.projectkeeper.plugin;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.projectkeeper.ProjectKeeper;

/**
 * Entry point for the {@code fix} goal.
 * <p>
 * Run using {@code mvn project-keeper:fix}
 * </p>
 */
@Mojo(name = "fix")
// [impl->dsn~mvn-fix-goal~1]
public class ProjectKeeperFixMojo extends AbstractProjectKeeperMojo {
    ProjectKeeperFixMojo() {
    }

    @Override
    protected void runProjectKeeper(final ProjectKeeper projectKeeper) throws MojoFailureException {
        final boolean success = projectKeeper.fix();
        if (!success) {
            throw new MojoFailureException("project-keeper:fix failed. See log messages above for details");
        }
    }
}
