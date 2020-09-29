package com.exasol.projectkeeper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Entry point for the fit goal.
 * <p>
 * Run using {@code mvn project-keeper:fit}
 * </p>
 */
@Mojo(name = "fit")
public class ProjectKeeperFitMojo extends AbstractProjectKeeperMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        new ProjectFilesFitter(getLog()).fitProjectStructure(this.project.getBasedir(), getModules());
    }
}
