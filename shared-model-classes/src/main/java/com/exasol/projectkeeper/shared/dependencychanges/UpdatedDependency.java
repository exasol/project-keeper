package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;

/**
 * This class represents an updated dependency.
 */
@Data
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

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
