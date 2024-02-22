package com.exasol.projectkeeper.plugin;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.projectkeeper.ProjectKeeper;

/**
 * Entry point for the {@code update-dependencies} goal.
 * <p>
 * Run using {@code mvn project-keeper:update-dependencies}
 * </p>
 */
@Mojo(name = "update-dependencies")
public class ProjectKeeperUpdateDependenciesMojo extends AbstractProjectKeeperMojo {

    @Override
    protected void runProjectKeeper(final ProjectKeeper projectKeeper) throws MojoFailureException {
        final boolean success = projectKeeper.updateDependencies();
        if (!success) {
            throw new MojoFailureException(
                    "project-keeper:update-dependencies failed. See log messages above for details");
        }
    }
}
