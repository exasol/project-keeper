package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.ProjectKeeperModule;

/**
 * This is an interface for classes collects information about a source projects. For example about a pom source.
 */
public interface AnalyzedSource {
    /**
     * Get Teh path to the sources build file.
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
     * Get if this source should be advertised.
     * 
     * @return {@code true} if this source should be advertised. For example in the README.md
     */
    boolean isAdvertise();
}
