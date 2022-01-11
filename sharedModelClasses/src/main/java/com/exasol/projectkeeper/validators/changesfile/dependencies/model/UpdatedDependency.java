package com.exasol.projectkeeper.validators.changesfile.dependencies.model;

import lombok.*;

/**
 * This class represents an updated dependency.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedDependency implements DependencyChange {
    private String groupId;
    private String artifactId;
    private String version;
    private String newVersion;

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
