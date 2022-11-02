package com.exasol.projectkeeper.shared.dependencies;

/** Base for {@link ProjectDependency} and {@link VersionedDependency} */
public interface BaseDependency {
    /** @return type of the dependency */
    Type getType();

    /** @return name of the module of the dependency */
    String getName();

    /** Type of the dependency */
    public enum Type {
        /** Compile dependency. */
        COMPILE("Compile Dependency Updates"),
        /** Runtime dependency */
        RUNTIME("Runtime Dependency Updates"),
        /** Test dependency */
        TEST("Test Dependency Updates"),
        /** Plugin */
        PLUGIN("Plugin Dependency Updates"),
        /** Development */
        DEV("Development Dependency Updates"),
        /** Unknown used for removed Golang dependencies as Go cannot detect the type anymore. */
        UNKNOWN("Other Dependency Updates");

        private final String header;

        private Type(final String header) {
            this.header = header;
        }

        /** @return header for reporting dependencies of the current type. */
        public String getHeader() {
            return this.header;
        }
    }
}
