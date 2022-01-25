package com.exasol.projectkeeper.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * Entry point for the fix goal.
 * <p>
 * Run using {@code mvn project-keeper:fix}
 * </p>
 */
@Mojo(name = "fix")
// [impl->dsn~mvn-fix-goal~1]
public class ProjectKeeperFixMojo extends AbstractProjectKeeperMojo {
    @Override
    public void execute() throws MojoExecutionException {
        if (isEnabled()) {
            final boolean success = getProjectKeeper().fix();
            if (!success) {
                throw new MojoExecutionException("project-keeper:fix failed. See above");
            }
        }
    }
}
