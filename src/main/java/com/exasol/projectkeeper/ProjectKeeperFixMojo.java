package com.exasol.projectkeeper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;

import com.exasol.projectkeeper.validators.pom.PomFileIO;

/**
 * Entry point for the fix goal.
 * <p>
 * Run using {@code mvn project-keeper:fix}
 * </p>
 */
@Mojo(name = "fix")
public class ProjectKeeperFixMojo extends AbstractProjectKeeperMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();
        final PomFileIO pomFile = getPomFile();
        getValidators(pomFile).forEach(validator -> validator.validate(finding -> finding.getFix().fixError(log)));
        pomFile.writeChanges();
    }
}
