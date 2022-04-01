package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;

/**
 * This class represents a removed dependency.
 */
@Data
public final class RemovedDependency implements DependencyChange {
    private final String groupId;
    private final String artifactId;
    private final String version;

    @JsonbCreator
    public RemovedDependency(@JsonbProperty("groupId") final String groupId,
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
