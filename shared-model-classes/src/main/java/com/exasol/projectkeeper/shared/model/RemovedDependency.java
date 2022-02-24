package com.exasol.projectkeeper.shared.model;

import lombok.*;

/**
 * This class represents a removed dependency.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovedDependency implements DependencyChange {
    private String groupId;
    private String artifactId;
    private String version;

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
