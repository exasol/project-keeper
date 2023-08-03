package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;
import java.util.Objects;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;

/**
 * This class represents a project with its dependencies.
 */
public final class ProjectWithDependencies {
    private final String projectName;
    private final List<ProjectDependency> dependencies;

    /**
     * Create a new instance.
     * 
     * @param projectName  project name
     * @param dependencies dependencies
     */
    public ProjectWithDependencies(final String projectName, final List<ProjectDependency> dependencies) {
        this.projectName = projectName;
        this.dependencies = dependencies;
    }

    /**
     * Create a new instance with updated dependencies.
     * 
     * @param dependencies updated dependencies
     * @return a new instance
     */
    public ProjectWithDependencies withDependencies(final List<ProjectDependency> dependencies) {
        return new ProjectWithDependencies(this.projectName, dependencies);
    }

    /** @return project name */
    public String getProjectName() {
        return projectName;
    }

    /** @return dependencies */
    public List<ProjectDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public String toString() {
        return "ProjectWithDependencies [projectName=" + projectName + ", dependencies=" + dependencies + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, dependencies);
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
        final ProjectWithDependencies other = (ProjectWithDependencies) obj;
        return Objects.equals(projectName, other.projectName) && Objects.equals(dependencies, other.dependencies);
    }
}
