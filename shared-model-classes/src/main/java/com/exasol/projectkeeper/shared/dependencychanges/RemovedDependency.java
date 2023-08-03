package com.exasol.projectkeeper.shared.dependencychanges;

import java.util.Objects;

import javax.annotation.processing.Generated;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * This class represents a removed dependency.
 */
public final class RemovedDependency implements DependencyChange {
    /**
     * The group ID of the removed dependency. May be null if the package system does not use group IDs (e.g. for
     * Golang).
     */
    private final String groupId;
    /**
     * The artifact ID of the removed dependency.
     */
    private final String artifactId;
    /**
     * The version of the removed dependency.
     */
    private final String version;

    /**
     * Create a new instance.
     * 
     * @param groupId    the group ID of the removed dependency. May be null if the package system does not use group
     *                   IDs (e.g. for Golang)
     * @param artifactId the artifact ID of the removed dependency
     * @param version    the version of the removed dependency
     */
    @JsonbCreator
    public RemovedDependency(@JsonbProperty("groupId") final String groupId,
            @JsonbProperty("artifactId") final String artifactId, @JsonbProperty("version") final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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

    @Override
    public String toString() {
        return "RemovedDependency [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version + "]";
    }

    @Override
    @Generated("vscode")
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version);
    }

    @Override
    @Generated("vscode")
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RemovedDependency other = (RemovedDependency) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(artifactId, other.artifactId)
                && Objects.equals(version, other.version);
    }
}
