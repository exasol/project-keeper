package com.exasol.projectkeeper.plugin;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.projectkeeper.ProjectKeeper;

/**
 * Entry point for the {@code verify-release} goal.
 * <p>
 * Run using {@code mvn project-keeper:verify-release}
 * </p>
 */
@Mojo(name = "verify-release")
public class ProjectKeeperVerifyReleaseMojo extends AbstractProjectKeeperMojo {
    ProjectKeeperVerifyReleaseMojo() {
    }

    @Override
    protected void runProjectKeeper(final ProjectKeeper projectKeeper) throws MojoFailureException {
        final boolean success = projectKeeper.verifyRelease();
        if (!success) {
            throw new MojoFailureException("project-keeper:verify-release failed. See log messages above for details");
        }
    }
}
