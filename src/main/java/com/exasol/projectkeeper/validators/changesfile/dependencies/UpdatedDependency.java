package com.exasol.projectkeeper.validators.changesfile.dependencies;

import java.util.Objects;

/**
 * This class represents an updated dependency.
 */
public class UpdatedDependency extends DependencyChange {
    private final String newVersion;

    /**
     * Create a new instance of {@link UpdatedDependency}.
     * 
     * @param groupId    group id
     * @param artifactId artifact id
     * @param version    old version
     * @param newVersion new version
     */
    protected UpdatedDependency(final String groupId, final String artifactId, final String version,
            final String newVersion) {
        super(groupId, artifactId, version);
        this.newVersion = newVersion;
    }

    /**
     * Get the new dependency version.
     * 
     * @return new dependency version
     */
    public String getNewVersion() {
        return this.newVersion;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        if (!super.equals(other))
            return false;
        final UpdatedDependency that = (UpdatedDependency) other;
        return Objects.equals(this.newVersion, that.newVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.newVersion, this.getClass().getName());
    }

    @Override
    public void accept(final DependencyChangeVisitor visitor) {
        visitor.visit(this);
    }
}
