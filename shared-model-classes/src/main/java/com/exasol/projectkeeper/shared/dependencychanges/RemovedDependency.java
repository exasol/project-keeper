package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * This class represents a removed dependency.
 * 
 * @param groupId    the group ID of the removed dependency. May be null if the package system does not use group IDs
 *                   (e.g. for Golang)
 * @param artifactId the artifact ID of the removed dependency
 * @param version    the version of the removed dependency
 */
public record RemovedDependency(@JsonbProperty("groupId") String groupId,
        @JsonbProperty("artifactId") String artifactId, @JsonbProperty("version") String version)
        implements DependencyChange {

    /**
     * Create a new instance.
     * 
     * @param groupId    the group ID of the removed dependency. May be null if the package system does not use group
     *                   IDs (e.g. for Golang)
     * @param artifactId the artifact ID of the removed dependency
     * @param version    the version of the removed dependency
     */
    @SuppressWarnings("java:S6207") // Redundant constructor required to deserialize JSON
    @JsonbCreator
    public RemovedDependency {
        // Required to deserialize JSON
    }

    /** @return group id */
    @Override
    public String getGroupId() {
        return groupId;
    }

    /** @return artifact id */
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    /** @return version */
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
