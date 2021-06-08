package com.exasol.projectkeeper.pom;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.*;

import com.exasol.errorreporting.ExaError;

/**
 * Implementation of {@link MavenFileProjectReader} using the {@link ProjectBuilder} that is injected by Maven core to
 * Mojo.
 */
public class DefaultMavenFileProjectReader implements MavenFileProjectReader {
    private final ProjectBuilder mavenProjectBuilder;
    private final MavenSession session;

    /**
     * Create a new instance of {@link DefaultMavenFileProjectReader}.
     *
     * @param mavenProjectBuilder maven project builder
     * @param session             maven session
     */
    public DefaultMavenFileProjectReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session) {
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.session = session;
    }

    @Override
    public MavenProject readModel(final File pomFile) throws ReadFailedException {
        try {
            final ProjectBuildingResult build = this.mavenProjectBuilder.build(pomFile,
                    this.session.getProjectBuildingRequest());
            return build.getProject();
        } catch (final ProjectBuildingException exception) {
            throw new ReadFailedException(
                    ExaError.messageBuilder("E-PK-46").message("Failed to build maven model.").toString(), exception);
        }
    }
}
