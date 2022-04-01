package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;

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
     * Get a human-readable project name.
     * <p>
     * Usually that's a version of the package name or artifactId without dashes and underscores.
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
     * Get the type of this source.
     *
     * @return source type.
     */
    SourceType getType();
}
