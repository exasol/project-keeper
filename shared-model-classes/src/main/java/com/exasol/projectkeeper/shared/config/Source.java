package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 * Project sources. For example a maven project
 */
@Data
@Builder
public final class Source {
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