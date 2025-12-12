package com.exasol.projectkeeper.shared.dependencies;

import static java.util.Objects.requireNonNull;

/**
 * This class represents an abstract dependency enabling to create a report of dependency changes later on.
 * 
 * @param type       type of the dependency
 * @param name       name of the module of the dependency
 * @param version    version of the dependency
 * @param isIndirect {@code true} if the dependency is indirect, i.e. transitive
 */
public record VersionedDependency(Type type, String name, String version, boolean isIndirect)
        implements BaseDependency {

    public VersionedDependency {
        requireNonNull(type, "type");
        requireNonNull(name, "name");
        requireNonNull(version, "version");
    }

    /**
     * Create a new builder.
     * 
     * @return a builder for new instances of {@link VersionedDependency}
     */
    public static Builder builder() {
        return new Builder();
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
    @Override
    public boolean isIndirect() {
        return this.isIndirect;
    }

    /** Builder for a new instance of {@link VersionedDependency} */
    public static final class Builder {
        private Type type;
        private String name;
        private boolean isIndirect;
        private String version;

        private Builder() {
        }

        /**
         * Set the dependency type.
         * 
         * @param type {@link Type} of the dependency
         * @return this for fluent programming
         */
        public Builder type(final Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set the dependency name.
         * 
         * @param name name of the dependency's module
         * @return this for fluent programming
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the dependency version.
         * 
         * @param version version of the dependency's module
         * @return this for fluent programming
         */
        public Builder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * Set indirect flag.
         * 
         * @param isIndirect {@code true} if the dependency is indirect, i.e. transitive
         * @return this for fluent programming
         */
        public Builder isIndirect(final boolean isIndirect) {
            this.isIndirect = isIndirect;
            return this;
        }

        /**
         * Create a new instance.
         * 
         * @return new instance of {@link VersionedDependency}
         */
        public VersionedDependency build() {
            return new VersionedDependency(type, name, version, isIndirect);
        }
    }
}
