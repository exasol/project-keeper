package com.exasol.projectkeeper.pom;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.project.*;

/**
 * Read a maven {@link Model} from an artifact of the maven repository using the {@link ProjectBuilder} that is injected
 * by Maven core to Mojo.
 */
public class MavenModelFromRepositoryReader {
    private static final String POM_ARTIFACT_TYPE = "pom";
    private final ProjectBuilder mavenProjectBuilder;
    private final ArtifactHandlerManager artifactHandlerManager;
    private final ProjectBuildingRequest projectBuildingRequest;

    /**
     * Create a new instance of {@link MavenModelFromRepositoryReader}.
     *
     * @param mavenProjectBuilder    maven project builder
     * @param session                maven session
     * @param artifactHandlerManager maven artifact handler manager
     */
    public MavenModelFromRepositoryReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session,
            final ArtifactHandlerManager artifactHandlerManager) {
        this.mavenProjectBuilder = requireNonNull(mavenProjectBuilder, "mavenProjectBuilder");
        this.artifactHandlerManager = requireNonNull(artifactHandlerManager, "artifactHandlerManager");
        requireNonNull(session, "session");
        this.projectBuildingRequest = new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());
        this.projectBuildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL)
                .setResolveDependencies(false).setProcessPlugins(false);
    }

    /**
     * Read the {@link Model} of an artifact of the maven repository.
     *
     * @param artifactId         maven artifact id
     * @param groupId            maven groupId
     * @param version            version
     * @param remoteRepositories list of remote repositories
     * @return read maven model
     * @throws ProjectBuildingException if reading fails
     */
    @SuppressWarnings("deprecation") // Maven class ArtifactRepository is deprecated
    public Model readModel(final String artifactId, final String groupId, final String version,
            final List<ArtifactRepository> remoteRepositories) throws ProjectBuildingException {
        final Artifact artifactDescription = new DefaultArtifact(groupId, artifactId, version, null,
                POM_ARTIFACT_TYPE, null, this.artifactHandlerManager.getArtifactHandler(POM_ARTIFACT_TYPE));
        this.projectBuildingRequest.setRemoteRepositories(remoteRepositories);
        final ProjectBuildingResult build = this.mavenProjectBuilder.build(artifactDescription, true,
                this.projectBuildingRequest);
        return build.getProject().getModel();
    }
}
