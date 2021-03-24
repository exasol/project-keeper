package com.exasol.projectkeeper.pom;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.project.*;
import org.apache.maven.repository.RepositorySystem;

/**
 * Implementation of {@link MavenArtifactModelReader} using the {@link ProjectBuilder} that is injected by Maven core to
 * Mojo.
 */
public class DefaultMavenArtifactModelReader implements MavenArtifactModelReader {
    private final ProjectBuilder mavenProjectBuilder;
    private final MavenSession session;
    private final RepositorySystem repositorySystem;

    /**
     * Create a new instance of {@link DefaultMavenArtifactModelReader}.
     * 
     * @param mavenProjectBuilder maven project builder
     * @param session             maven session
     * @param repositorySystem    maven repository system
     */
    public DefaultMavenArtifactModelReader(final ProjectBuilder mavenProjectBuilder, final MavenSession session,
            final RepositorySystem repositorySystem) {
        this.mavenProjectBuilder = mavenProjectBuilder;
        this.session = session;
        this.repositorySystem = repositorySystem;
    }

    @Override
    public Model readModel(final String artifactId, final String groupId, final String version)
            throws ProjectBuildingException {
        final Artifact artifact = this.repositorySystem.createProjectArtifact(groupId, artifactId, version);
        final ProjectBuildingResult build = this.mavenProjectBuilder.build(artifact,
                this.session.getProjectBuildingRequest());
        return build.getProject().getModel();
    }
}
