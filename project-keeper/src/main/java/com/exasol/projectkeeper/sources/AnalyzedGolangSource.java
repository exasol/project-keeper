package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

import lombok.Builder;
import lombok.Data;

/**
 * {@link AnalyzedSource} for Go source.
 */
@Data
@Builder
public class AnalyzedGolangSource implements AnalyzedSource {
    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String moduleName;
    private final String projectName;
    private final String version;
    private final DependencyChangeReport dependencyChanges;
    private final ProjectDependencies dependencies;
    /** {@code true} if this is the main Go project in the repo (if go.mod lies directly in repo) */
    private final boolean isRootProject;

    @Override
    public SourceType getType() {
        return SourceType.GOLANG;
    }
}
