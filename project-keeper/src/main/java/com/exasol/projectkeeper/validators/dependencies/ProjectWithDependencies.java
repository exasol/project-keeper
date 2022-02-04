package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;

import com.exasol.projectkeeper.dependencies.ProjectDependency;

import lombok.Data;
import lombok.With;

/**
 * This class represents a project with its dependencies.
 */
@Data
public class ProjectWithDependencies {
    private final String projectName;
    @With
    private final List<ProjectDependency> dependencies;
}
