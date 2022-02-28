package com.exasol.projectkeeper.shared.dependencies;

import java.util.List;

import lombok.*;

/**
 * The dependencies of a project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDependencies {
    private List<ProjectDependency> dependencies;
}
