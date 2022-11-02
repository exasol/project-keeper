package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.List;

import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

/**
 * This data class represents information about a Go module that was generated by executing the {@code go list} command
 * for getting the direct dependencies.
 */
class GoModule {
    private final String name;
    private final List<VersionedDependency> dependencies;

    GoModule(final String name, final List<VersionedDependency> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    /**
     * @return name of the module
     */
    String getName() {
        return this.name;
    }

    /**
     * @return list of dependencies of this module
     */
    List<VersionedDependency> getDependencies() {
        return this.dependencies;
    }

}