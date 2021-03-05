package com.exasol.projectkeeper.validators.changelog.dependencies;

import java.util.Objects;

/**
 * This class represents a removed dependency.
 */
public class DependencyRemove extends DependencyChange {

    /**
     * Create a new instance of {@link DependencyRemove}.
     * 
     * @param groupId    group id
     * @param artifactId artifact id
     * @param version    version
     */
    protected DependencyRemove(final String groupId, final String artifactId, final String version) {
        super(groupId, artifactId, version);
    }

    @Override
    public boolean equals(final Object other) {
        return super.equals(other) && other instanceof DependencyRemove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getClass().getName());
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
