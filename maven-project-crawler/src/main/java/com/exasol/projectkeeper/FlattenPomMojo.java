package com.exasol.projectkeeper;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.exasol.errorreporting.ExaError;

/**
 * This mojo prints the effective pom of a project. That means that it integrates the parent pom.
 */
@Mojo(name = "getFlatPom")
public class FlattenPomMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * Entry point.
     * 
     * @throws MojoExecutionException if something goes wrong
     */
    @Override
    public void execute() throws MojoExecutionException {
        final Model model = this.project.getModel();
        new ResponseEncoder().printResponse(convertToString(model));
    }

    private String convertToString(final Model model) throws MojoExecutionException {
        try (final StringWriter stringWriter = new StringWriter();) {
            new MavenXpp3Writer().write(stringWriter, model);
            return stringWriter.toString();
        } catch (final IOException exception) {
            throw new MojoExecutionException(
                    ExaError.messageBuilder("E-PK-MPK-1").message("Failed to serialize mvn model.").toString(),
                    exception);
        }
    }
}
