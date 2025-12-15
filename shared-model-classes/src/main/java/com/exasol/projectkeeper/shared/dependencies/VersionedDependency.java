package com.exasol.projectkeeper.shared.dependencies;

/** This class represents an abstract dependency enabling to create a report of dependency changes later on. */
public class VersionedDependency implements BaseDependency {

    /**
     * Create a new builder.
     * 
     * @return a builder for new instances of {@link VersionedDependency}
     */
    public static Builder builder() {
        return new Builder();
    }

    private Type type;
    private String name;
    private String version;
    private boolean isIndirect;

    VersionedDependency() {
    }

    /**
     * Get the dependency's type.
     * 
     * @return type of the dependency
     */
    @Override
    public Type getType() {
        return this.type;
    }

    /**
     * Get the dependency's name.
     * 
     * @return name of the module of the dependency
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get the dependency's version.
     * 
     * @return version of the dependency
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Check if the dependency is indirect.
     * 
     * @return {@code true} if the dependency is indirect, i.e. transitive
     */
    public boolean isIndirect() {
        return this.isIndirect;
    }

    /** Builder for a new instance of {@link VersionedDependency} */
    public static final class Builder {
        private final VersionedDependency dependency = new VersionedDependency();

        private Builder() {
        }

        /**
         * Set the dependency type.
         * 
         * @param type {@link Type} of the dependency
         * @return this for fluent programming
         */
        public Builder type(final Type type) {
            this.dependency.type = type;
            return this;
        }

        /**
         * Set the dependency name.
         * 
         * @param name name of the dependency's module
         * @return this for fluent programming
         */
        public Builder name(final String name) {
            this.dependency.name = name;
            return this;
        }

        /**
         * Set the dependency version.
         * 
         * @param version version of the dependency's module
         * @return this for fluent programming
         */
        public Builder version(final String version) {
            this.dependency.version = version;
            return this;
        }

        /**
         * Set indirect flag.
         * 
         * @param isIndirect {@code true} if the dependency is indirect, i.e. transitive
         * @return this for fluent programming
         */
        public Builder isIndirect(final boolean isIndirect) {
            this.dependency.isIndirect = isIndirect;
            return this;
        }

        /**
         * Create a new instance.
         * 
         * @return new instance of {@link VersionedDependency}
         */
        public VersionedDependency build() {
            return this.dependency;
        }
    }
}
