package com.exasol.projectkeeper.config;

import java.nio.file.Path;
import java.util.*;

import com.exasol.projectkeeper.ProjectKeeperModule;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents the project-keeper configuration.
 */
@Data
@Builder
public class ProjectKeeperConfig {
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

    /**
     * Enum with types for projects
     */
    public enum SourceType {
        /** Type for maven projects */
        MAVEN
    }

    /**
     * Project sources. For example a maven project
     */
    @Data
    @Builder
    public static class Source {
        /** Path to the source-project root or build file. Example: {@code my-project/pom.xml}. */
        private final Path path;
        /** Type if the source-project Example: {@code MAVEN}. */
        private final SourceType type;
        /** List with project-keeper modules */
        // [impl->dsn~modules~1]
        @Builder.Default
        private final Set<ProjectKeeperModule> modules = Collections.emptySet();
        /** List of regular expressions that match validation messages to exclude specifically for this source */
        // [impl->dsn~excluding~1]
        @Builder.Default
        private final List<String> excludes = Collections.emptyList();
    }
}
