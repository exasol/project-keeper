package com.exasol.projectkeeper.validators;

import java.io.File;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.*;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads a maven {@link Model} from pom file.
 * <p>
 * In contrast to the plain {@link MavenXpp3Reader} this class also resolves parses the model and resolves properties
 * and plugin versions.
 * </p>
 */
public class MavenInjectedMavenModelReader implements MavenModelReader {

    private final ProjectBuilder mavenProjectBuilder;
    private final MavenSession session;

    /**
     * Create a new instance of {@link MavenInjectedMavenModelReader}.
     * 
     * @param mavenProjectBuilder maven project builder
     * @param session             maven session
     */
    public MavenInjectedMavenModelReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session) {
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.session = session;
    }

    @Override
    public Model readModel(final File pomFile) throws ReadFailedException {
        try {
            final ProjectBuildingResult build = this.mavenProjectBuilder.build(pomFile,
                    this.session.getProjectBuildingRequest());
            return build.getProject().getModel();
        } catch (final ProjectBuildingException exception) {
            throw new ReadFailedException(
                    ExaError.messageBuilder("E-PK-46").message("Failed to build maven model.").toString(), exception);
        }
    }
}
