package com.exasol.projectkeeper;

import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Entry point for the fix goal.
 * <p>
 * Run using {@code mvn project-keeper:fix}
 * </p>
 */
@Mojo(name = "fix")
public class ProjectKeeperFixMojo extends AbstractProjectKeeperMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final List<String> modules = getModules();
        new ProjectFilesFixer(getLog()).fixProjectStructure(this.project.getBasedir(), modules);
        new PomFileTemplateRunner(this.project.getModel().getPomFile()).fix(getLog(), modules);
    }
}
