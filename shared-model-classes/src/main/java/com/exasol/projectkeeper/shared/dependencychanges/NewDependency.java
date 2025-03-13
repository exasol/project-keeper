package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * This class represents an added dependency.
 * 
 * @param groupId    the group ID of the added dependency. May be null if the package system does not use group IDs
 *                   (e.g. for Golang)
 * @param artifactId the artifact ID of the added dependency
 * @param version    the version of the added dependency
 */
public record NewDependency(@JsonbProperty("groupId") String groupId, @JsonbProperty("artifactId") String artifactId,
        @JsonbProperty("version") String version) implements DependencyChange {

    /**
     * Create a new instance
     * 
     * @param groupId    the group ID of the added dependency. May be null if the package system does not use group IDs
     *                   (e.g. for Golang)
     * @param artifactId the artifact ID of the added dependency
     * @param version    the version of the added dependency
     */
    @JsonbCreator
    public NewDependency {
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
