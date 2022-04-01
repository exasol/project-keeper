package com.exasol.projectkeeper.shared.dependencychanges;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import lombok.Data;

/**
 * This class represents an updated dependency.
 */
@Data
public final class UpdatedDependency implements DependencyChange {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String newVersion;

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
