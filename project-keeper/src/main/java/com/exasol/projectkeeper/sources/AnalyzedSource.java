package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.ApStyleFormatter;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

/**
 * This is an interface for classes that collects information about a source project, e.g. about a pom source.
 */
public interface AnalyzedSource {
    /**
     * Get the path to the sources build file.
     *
     * @return path
     */
    Path getPath();

    /**
     * Get the enabled modules for the source.
     *
     * @return set of enabled modules
     */
    Set<ProjectKeeperModule> getModules();

    /**
     * Get project name for documentation files, such as changes file, readme and the like.
     * <p>
     * Usually that's something like the package name or artifactId. Project keeper additionally will apply
     * {@link ApStyleFormatter} to generate a human-readable format.
     * </p>
     *
     * @return project name
     */
    String getProjectName();

    /**
     * Get the version of this source.
     *
     * @return {@code null if version could not be detected}
     */
    String getVersion();

    /**
     * Check if this source should be advertised.
     *
     * @return {@code true} if this source should be advertised, e.g. in the README.md
     */
    boolean isAdvertise();

    /**
     * Get a report of all added/updated/removed dependencies.
     *
     * @return dependency change report
     */
    DependencyChangeReport getDependencyChanges();

    /**
     * Get a list of all dependencies incl. licenses.
     *
     * @return dependencies
     */
    ProjectDependencies getDependencies();
}
