package com.exasol.projectkeeper.sources;

import java.nio.file.Path;
import java.util.Set;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.RepoNameReader;

import lombok.Builder;
import lombok.Data;

/**
 * {@link AnalyzedSource} for non-maven source.
 */
@Data
@Builder
public class AnalyzedSourceImpl implements AnalyzedSource {

    /**
     * @param source source of main or sub module within the current project
     * @return {@code true} if source represents the root project
     */
    public static boolean isRoot(final Source source) {
        return source.getPath().getParent() == null;
    }

    /**
     * @param projectDir root folder of the project
     * @param source     source of main or sub module within the current project
     * @return name of the repository hosting the current project.
     */
    public static String projectName(final Path projectDir, final Source source) {
        if (isRoot(source)) {
            return RepoNameReader.getRepoName(projectDir);
        } else {
            return RepoNameReader.getRepoName(source.getPath().getParent());
        }
    }

    private final Path path;
    private final Set<ProjectKeeperModule> modules;
    private final boolean advertise;
    private final String moduleName;
    private final String projectName;
    private final String version;
    private final DependencyChangeReport dependencyChanges;
    private final ProjectDependencies dependencies;
    /** {@code true} if this is the main project in the repo, i.e. if build script (e.g. go.mod) lies directly in repo. */
    private final boolean isRootProject;
}
