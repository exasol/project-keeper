package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.ProjectKeeperModule;

import lombok.Data;

/**
 * {@link AnalyzedSource} for maven source.
 */
@Data
public class AnalyzedMavenSource implements AnalyzedSource {
    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String artifactId;
    private final String projectName;
    /** {@code true} if this is the main maven project in the repo (if pom lies directly in repo) */
    private final boolean isRootProject;
}
