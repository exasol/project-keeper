package com.exasol.projectkeeper.shared.dependencies;

/**
 * Base for {@link ProjectDependency} and versioned dependency
 */
public interface BaseDependency {
    /**
     * @return type of the dependency
     */
    Type getType();

    /**
     * @return name of the module of the dependency
     */
    String getName();

    /**
     * Type of a {@link ProjectDependency}.
     */
    public enum Type {
        /** Compile dependency. */
        COMPILE,
        /** Runtime dependency */
        RUNTIME,
        /** Test dependency */
        TEST,
        /** Plugin */
        PLUGIN
    }
}
