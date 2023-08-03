package com.exasol.projectkeeper.shared.config;

import lombok.Data;

/**
 * Reference to a parent pom of a maven source.
 */
@Data
public final class ParentPomRef {
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final String relativePath;
}