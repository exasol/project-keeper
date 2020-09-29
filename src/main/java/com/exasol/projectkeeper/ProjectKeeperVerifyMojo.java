package com.exasol.projectkeeper;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Entry point for the verify goal.
 * <p>
 * Run using {@code mvn project-keeper:verify}
 * </p>
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.PACKAGE)
public class ProjectKeeperVerifyMojo extends AbstractProjectKeeperMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final File projectBaseDirectory = this.project.getBasedir();
        final boolean filesValidationResult = new ProjectFilesValidator(getLog())
                .validateProjectStructure(projectBaseDirectory, getModules());
        if (!filesValidationResult) {
            throw new MojoFailureException("This projects structure does not conform with the template.\n"
                    + "You can automatically fix it by running mvn project-keeper:fit");
        }
    }
}
