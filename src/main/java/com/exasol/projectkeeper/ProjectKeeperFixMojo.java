package com.exasol.projectkeeper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
    public void execute() throws MojoExecutionException, MojoFailureException {
        final var log = getLog();
        getValidators().forEach(validator -> validator.validate().forEach(finding -> finding.getFix().fixError(log)));
    }
}
