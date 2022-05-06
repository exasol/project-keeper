package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import lombok.Builder;
import lombok.Data;

/**
 * {@link AnalyzedSource} for maven source.
 */
@Data
@Builder
public class AnalyzedMavenSource implements AnalyzedSource {
    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String artifactId;
    private final String projectName;
    private final String version;
    private final DependencyChangeReport dependencyChanges;
    private final ProjectDependencies dependencies;
    /** {@code true} if this is the main maven project in the repo (if pom lies directly in repo) */
    private final boolean isRootProject;
}
