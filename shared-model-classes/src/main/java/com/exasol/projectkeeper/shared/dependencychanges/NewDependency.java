package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;

/**
 * This class represents an added dependency.
 */
@Data
public final class NewDependency implements DependencyChange {
    /**
     * The group ID of the added dependency. May be null if the package system does not use group IDs (e.g. for Golang).
     */
    private final String groupId;
    /**
     * The artifact ID of the added dependency.
     */
    private final String artifactId;
    /**
     * The version of the added dependency.
     */
    private final String version;

    /**
     * Create a new instance.
     * 
     * @param groupId    the group ID of the added dependency. May be null if the package system does not use group IDs
     *                   (e.g. for Golang)
     * @param artifactId the artifact ID of the added dependency
     * @param version    the version of the added dependency
     */
    @JsonbCreator
    public NewDependency(@JsonbProperty("groupId") final String groupId,
            @JsonbProperty("artifactId") final String artifactId, @JsonbProperty("version") final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
