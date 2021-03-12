package com.exasol.projectkeeper.validators.changesfile.dependencies;

import java.util.Objects;

/**
 * This class represents a removed dependency.
 */
public class RemovedDependency extends DependencyChange {

    /**
     * Create a new instance of {@link RemovedDependency}.
     * 
     * @param groupId    group id
     * @param artifactId artifact id
     * @param version    version
     */
    protected RemovedDependency(final String groupId, final String artifactId, final String version) {
        super(groupId, artifactId, version);
    }

    @Override
    public boolean equals(final Object other) {
        return super.equals(other) && other instanceof RemovedDependency;
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
