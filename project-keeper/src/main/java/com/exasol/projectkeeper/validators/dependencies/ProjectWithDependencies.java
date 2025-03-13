package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;

/**
 * This class represents a project with its dependencies.
 * 
 * @param projectName  project name
 * @param dependencies dependencies
 */
public record ProjectWithDependencies(String projectName, List<ProjectDependency> dependencies) {

    /**
     * Create a new instance with updated dependencies.
     * 
     * @param dependencies updated dependencies
     * @return a new instance
     */
    public ProjectWithDependencies withDependencies(final List<ProjectDependency> dependencies) {
        return new ProjectWithDependencies(this.projectName, dependencies);
    }

    /**
     * Get project name.
     * 
     * @return project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Get dependencies.
     * 
     * @return dependencies
     */
    public List<ProjectDependency> getDependencies() {
        return dependencies;
    }
}
