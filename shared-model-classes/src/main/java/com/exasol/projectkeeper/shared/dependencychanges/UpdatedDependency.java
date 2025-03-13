package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbProperty;

/**
 * This class represents an updated dependency.
 * 
 * @param groupId    the group ID of the updated dependency. May be null if the package system does not use group IDs
 *                   (e.g. for Golang)
 * @param artifactId the artifact ID of the updated dependency
 * @param version    the old version of the updated dependency
 * @param newVersion the new version of the updated dependency
 */
public record UpdatedDependency(@JsonbProperty("groupId") String groupId,
        @JsonbProperty("artifactId") String artifactId, @JsonbProperty("version") String version,
        @JsonbProperty("newVersion") String newVersion) implements DependencyChange {

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

    /** @return previous version */
    @Override
    public String getVersion() {
        return version;
    }

    /**
     * Get new version.
     * 
     * @return new version
     */
    public String getNewVersion() {
        return newVersion;
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
