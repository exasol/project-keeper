package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;
import java.util.*;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents the project-keeper configuration.
 */
@Data
@Builder
public final class ProjectKeeperConfig {
    /** Lists with source-projects to crawl. */
    @Builder.Default
    private final List<Source> sources = Collections.emptyList();
    /** List of replacements for broken links. */
    @Builder.Default
    private final List<String> linkReplacements = Collections.emptyList();
    // [impl->dsn~excluding~1]
    /** List of regular expressions that match validation messages to exclude */
    @Builder.Default
    private final List<String> excludes = Collections.emptyList();
    private final VersionConfig versionConfig;

    /**
     * Enum with types for projects
     */
    public enum SourceType {
        /** Maven projects */
        MAVEN,
        /** Golang projects */
        GOLANG,
        /** NPM projects */
        NPM
    }

    /**
     * Interface for classes that tell PK how to find out the overall project version.
     */
    public interface VersionConfig {

        /**
         * Accept a visitor.
         *
         * @param visitor the visitor
         */
        public void accept(Visitor visitor);

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

    /**
     * Config for a provided version string.
     */
    @Data
    public static final class FixedVersion implements VersionConfig {
        private final String version;

        @Override
        public void accept(final Visitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Config that introduces PK to read the version from a source.
     */
    @Data
    public static final class VersionFromSource implements VersionConfig {
        private final Path pathToPom;

        @Override
        public void accept(final Visitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * Project sources. For example a maven project
     */
    @Data
    @Builder
    public static final class Source {
        /** Path to the source-project root or build file. Example: {@code my-project/pom.xml}. */
        private final Path path;
        /** Type if the source-project Example: {@code MAVEN}. */
        private final SourceType type;
        /** List with project-keeper modules */
        // [impl->dsn~modules~1]
        @Builder.Default
        private final Set<ProjectKeeperModule> modules = Collections.emptySet();
        @Builder.Default
        private final boolean advertise = true;
        /**
         * Reference to the parent pom. For maven sources only. {@code null} if not provided.
         */
        private final ParentPomRef parentPom;
    }

    /**
     * Reference to a parent pom of a maven source.
     */
    @Data
    public static final class ParentPomRef {
        private final String groupId;
        private final String artifactId;
        private final String version;
        private final String relativePath;
    }
}
