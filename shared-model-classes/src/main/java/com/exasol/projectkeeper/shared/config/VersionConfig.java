package com.exasol.projectkeeper.shared.config;

/**
 * Interface for classes that tell PK how to find out the overall project version.
 */
public interface VersionConfig {

    /**
     * Accept a visitor.
     *
     * @param visitor the visitor
     */
    public void accept(VersionConfig.Visitor visitor);

    /**
     * Visitor interface for {@link VersionConfig}.
     */
    public interface Visitor {

        /**
         * Visit.
         *
         * @param fixedVersion the fixed version
         */
        public void visit(FixedVersion fixedVersion);

        /**
         * Visit.
         *
         * @param versionFromMavenSource the version from maven source
         */
        public void visit(VersionFromSource versionFromMavenSource);
    }
}