package com.exasol.projectkeeper.validators.changesfile.dependencies.model;

import lombok.*;

/**
 * This class represents an added dependency.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewDependency implements DependencyChange {
    private String groupId;
    private String artifactId;
    private String version;

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
