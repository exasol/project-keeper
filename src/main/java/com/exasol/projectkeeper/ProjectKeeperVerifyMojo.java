package com.exasol.projectkeeper;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.exasol.projectkeeper.files.ProjectFilesValidator;
import com.exasol.projectkeeper.pom.PomFileValidationRunner;

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
        final Collection<ProjectKeeperModule> enabledModules = getEnabledModules();
        final boolean filesValidationResult = new ProjectFilesValidator(getLog())
                .validateProjectStructure(projectBaseDirectory, enabledModules);
        final boolean pomValidationResult = new PomFileValidationRunner(this.project.getModel().getPomFile())
                .verify(getLog(), enabledModules);
        if (!(filesValidationResult && pomValidationResult)) {
            throw new MojoFailureException("E-PK-6: This projects structure does not conform with the template.\n"
                    + "You can automatically fix it by running mvn project-keeper:fix");
        }
    }
}
