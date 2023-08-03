package com.exasol.projectkeeper.shared.dependencychanges;

import java.util.Objects;

import javax.annotation.processing.Generated;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

/**
 * This class represents an updated dependency.
 */
public final class UpdatedDependency implements DependencyChange {
    /**
     * The group ID of the updated dependency. May be null if the package system does not use group IDs (e.g. for
     * Golang).
     */
    private final String groupId;
    /**
     * The artifact ID of the updated dependency.
     */
    private final String artifactId;
    /**
     * The old version of the updated dependency.
     */
    private final String version;
    /**
     * The new version of the updated dependency.
     */
    private final String newVersion;

    /**
     * Create a new instance.
     * 
     * @param groupId    the group ID of the updated dependency. May be null if the package system does not use group
     *                   IDs (e.g. for Golang)
     * @param artifactId the artifact ID of the updated dependency
     * @param version    the old version of the updated dependency
     * @param newVersion the new version of the updated dependency
     */
    @JsonbCreator
    public UpdatedDependency(@JsonbProperty("groupId") final String groupId,
            @JsonbProperty("artifactId") final String artifactId, @JsonbProperty("version") final String version,
            @JsonbProperty("newVersion") final String newVersion) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.newVersion = newVersion;
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

    /** @return previous version */
    @Override
    public String getVersion() {
        return version;
    }

    /** @return new version */
    public String getNewVersion() {
        return newVersion;
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "UpdatedDependency [groupId=" + groupId + ", artifactId=" + artifactId + ", version=" + version
                + ", newVersion=" + newVersion + "]";
    }

    @Override
    @Generated("vscode")
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, newVersion);
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
        final UpdatedDependency other = (UpdatedDependency) obj;
        return Objects.equals(groupId, other.groupId) && Objects.equals(artifactId, other.artifactId)
                && Objects.equals(version, other.version) && Objects.equals(newVersion, other.newVersion);
    }
}
