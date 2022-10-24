package com.exasol.projectkeeper.shared.dependencies;

/**
 * Base for {@link ProjectDependency} and versioned dependency
 */
public abstract class BaseDependency {
    /** Dependency Type */
    protected Type type;
    /** Dependency name */
    protected String name;

    BaseDependency() {
    }

    /**
     * @param type
     * @param name
     */
    public BaseDependency(final Type type, final String name) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

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
