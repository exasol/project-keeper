package com.exasol.projectkeeper.pom;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.*;

import com.exasol.errorreporting.ExaError;

/**
 * Implementation of {@link MavenProjectFromFileReader} using the {@link ProjectBuilder} that is injected by Maven core
 * to Mojo.
 */
//[impl->dsn~reading-project-dependencies~1]
public class DefaultMavenProjectFromFileReader implements MavenProjectFromFileReader {
    private final ProjectBuilder mavenProjectBuilder;
    private final MavenSession session;

    /**
     * Create a new instance of {@link DefaultMavenProjectFromFileReader}.
     *
     * @param mavenProjectBuilder maven project builder
     * @param session             maven session
     */
    public DefaultMavenProjectFromFileReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session) {
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.session = session;
    }

    @Override
    public MavenProject readProject(final File pomFile) throws ReadFailedException {
        try {
            final ProjectBuildingRequest projectBuildingRequest = this.session.getProjectBuildingRequest();
            final ProjectBuildingResult build = this.mavenProjectBuilder.build(pomFile, projectBuildingRequest);
            return build.getProject();
        } catch (final ProjectBuildingException exception) {
            throw new ReadFailedException(
                    ExaError.messageBuilder("E-PK-MPC-46").message("Failed to build maven model.").toString(),
                    exception);
        }
    }
}
