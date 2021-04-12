package com.exasol.projectkeeper.pom;

import org.apache.maven.model.Model;
import org.apache.maven.project.ProjectBuildingException;

/**
 * Interface for readers that read a maven {@link Model} from an artifact of the maven repository.
 */
public interface MavenArtifactModelReader {

    /**
     * Read the {@link Model} of an artifact of the maven repository.
     *
     * @param artifactId maven artifact id
     * @param groupId    maven groupId
     * @param version    version
     * @return read maven model
     * @throws ProjectBuildingException if reading fails
     */
    public Model readModel(final String artifactId, final String groupId, final String version)
            throws ProjectBuildingException;
}
