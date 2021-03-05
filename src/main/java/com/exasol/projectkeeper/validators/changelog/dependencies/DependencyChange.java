package com.exasol.projectkeeper.validators.changelog.dependencies;

import java.util.Objects;

/**
 * This class is the abstract basis for classes that represent dependency changes (add; update or remove).
 */
public abstract class DependencyChange {
    private final String groupId;
    private final String artifactId;
    private final String version;

    /**
     * Create a new instance of {@link DependencyChange}.
     * 
     * @param groupId    artifact id
     * @param artifactId group id
     * @param version    version
     */
    protected DependencyChange(final String groupId, final String artifactId, final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Get the dependencies group id.
     * 
     * @return group id
     */
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * Get the artifact id.
     *
     * @return artifact id
     */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * Get the dependencies version.
     * 
     * @return version
     */
    public String getVersion() {
        return this.version;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        final DependencyChange that = (DependencyChange) other;
        return Objects.equals(this.groupId, that.groupId) && Objects.equals(this.artifactId, that.artifactId)
                && Objects.equals(this.version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupId, this.artifactId, this.version);
    }

    /**
     * Accept a {@link DependencyChangeVisitor}.
     * 
     * @param visitor visitor to accept
     */
    public abstract void accept(DependencyChangeVisitor visitor);
}
