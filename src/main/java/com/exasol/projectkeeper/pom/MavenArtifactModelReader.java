package com.exasol.projectkeeper.pom;

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.project.*;
import org.apache.maven.repository.RepositorySystem;

/**
 * Read a maven {@link Model} from an artifact of the maven repository using the {@link ProjectBuilder} that is injected
 * by Maven core to Mojo.
 */
public class MavenArtifactModelReader {
    private final ProjectBuilder mavenProjectBuilder;
    private final MavenSession session;
    private final RepositorySystem repositorySystem;

    /**
     * Create a new instance of {@link MavenArtifactModelReader}.
     * 
     * @param mavenProjectBuilder maven project builder
     * @param session             maven session
     * @param repositorySystem    maven repository system
     */
    public MavenArtifactModelReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session,
            final RepositorySystem repositorySystem) {
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.session = session;
        this.repositorySystem = repositorySystem;
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
    public Model readModel(final String artifactId, final String groupId, final String version,
            final List<ArtifactRepository> remoteRepositories) throws ProjectBuildingException {
        final Artifact artifactDescription = this.repositorySystem.createProjectArtifact(groupId, artifactId, version);
        final ProjectBuildingRequest projectBuildingRequest = this.session.getProjectBuildingRequest();
        projectBuildingRequest.setRemoteRepositories(remoteRepositories);
        final ProjectBuildingResult build = this.mavenProjectBuilder.build(artifactDescription, projectBuildingRequest);
        return build.getProject().getModel();
    }
}
