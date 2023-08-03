package com.exasol.projectkeeper.shared.config;

import java.util.Collections;
import java.util.List;

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
}
