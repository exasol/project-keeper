package com.exasol.projectkeeper.validators.changelog.dependencies;

import java.util.List;

/**
 * This class represents a report of changed dependencies.
 */
public class DependencyChangeReport {
    private final List<DependencyChange> compileDependencyChanges;
    private final List<DependencyChange> runtimeDependencyChanges;
    private final List<DependencyChange> testDependencyChanges;
    private final List<DependencyChange> pluginDependencyChanges;

    /**
     * Create a new instance of {@link DependencyChangeReport}
     * 
     * @param compileDependencyChanges dependency changes in compile scope
     * @param runtimeDependencyChanges dependency changes in runtime scope
     * @param testDependencyChanges    dependency changes in test scope
     * @param pluginDependencyChanges  dependency changes in plugin scope
     */
    public DependencyChangeReport(final List<DependencyChange> compileDependencyChanges,
            final List<DependencyChange> runtimeDependencyChanges, final List<DependencyChange> testDependencyChanges,
            final List<DependencyChange> pluginDependencyChanges) {
        this.compileDependencyChanges = compileDependencyChanges;
        this.runtimeDependencyChanges = runtimeDependencyChanges;
        this.testDependencyChanges = testDependencyChanges;
        this.pluginDependencyChanges = pluginDependencyChanges;
    }

    /**
     * Get the dependency changes of the compile scope.
     * 
     * @return list of dependency changes
     */
    public List<DependencyChange> getCompileDependencyChanges() {
        return this.compileDependencyChanges;
    }

    /**
     * Get the dependency changes of the runtime scope.
     *
     * @return list of dependency changes
     */
    public List<DependencyChange> getRuntimeDependencyChanges() {
        return this.runtimeDependencyChanges;
    }

    /**
     * Get the dependency changes of the test scope.
     *
     * @return list of dependency changes
     */
    public List<DependencyChange> getTestDependencyChanges() {
        return this.testDependencyChanges;
    }

    /**
     * Get the dependency changes of the plugin scope.
     *
     * @return list of dependency changes
     */
    public List<DependencyChange> getPluginDependencyChanges() {
        return this.pluginDependencyChanges;
    }
}
