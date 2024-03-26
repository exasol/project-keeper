package com.exasol.projectkeeper.shared.dependencies;

import java.util.List;
import java.util.Objects;

/**
 * The dependencies of a project.
 */
public final class ProjectDependencies {
    private List<ProjectDependency> dependencies;

    /**
     * Create a new instance.
     * <p>
     * Required for JSON deserializing.
     */
    public ProjectDependencies() {
        this(null);
    }

    /**
     * Create a new instance.
     * 
     * @param dependencies project dependencies.
     */
    public ProjectDependencies(final List<ProjectDependency> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Get project dependencies.
     * 
     * @return project dependencies
     */
    public List<ProjectDependency> getDependencies() {
        return dependencies;
    }

    /**
     * Get project dependencies.
     * <p>
     * Required for JSON deserializing.
     * 
     * @param dependencies project dependencies
     */
    public void setDependencies(final List<ProjectDependency> dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public String toString() {
        return "ProjectDependencies [dependencies=" + dependencies + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependencies);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectDependencies other = (ProjectDependencies) obj;
        return Objects.equals(dependencies, other.dependencies);
    }

}
