package com.exasol.projectkeeper.validators.changelog.dependencies;

import java.util.Objects;

/**
 * This class represents an added dependency.
 */
public class DependencyAdd extends DependencyChange {
    /**
     * Create a new instance of {@link DependencyAdd}.
     * 
     * @param groupId    group id
     * @param artifactId artifact id
     * @param version    version
     */
    protected DependencyAdd(final String groupId, final String artifactId, final String version) {
        super(groupId, artifactId, version);
    }

    @Override
    public boolean equals(final Object other) {
        return super.equals(other) && other instanceof DependencyAdd;
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
