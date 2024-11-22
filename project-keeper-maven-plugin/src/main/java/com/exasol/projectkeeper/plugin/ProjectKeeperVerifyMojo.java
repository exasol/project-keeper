package com.exasol.projectkeeper.plugin;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.projectkeeper.ProjectKeeper;

/**
 * Entry point for the {code verify} goal.
 * <p>
 * Run using {@code mvn project-keeper:verify}
 * </p>
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.PACKAGE)
// [impl->dsn~mvn-verify-goal~1]
public class ProjectKeeperVerifyMojo extends AbstractProjectKeeperMojo {
    ProjectKeeperVerifyMojo() {
    }

    @Override
    protected void runProjectKeeper(final ProjectKeeper projectKeeper) throws MojoFailureException {
        if (!projectKeeper.verify()) {
            throw new MojoFailureException("Validation failed. See above.");
        }
    }
}
