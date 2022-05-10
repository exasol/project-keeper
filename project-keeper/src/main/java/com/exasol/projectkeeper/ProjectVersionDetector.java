package com.exasol.projectkeeper;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.AnalyzedSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class extracts the overall project version.
 */
public class ProjectVersionDetector {
    private static final String FAILED_TO_DETECT_VERSION = "Failed to detect overall project version.";

    /**
     * Detect the version of the overall project.
     * 
     * @param config          project-keeper configuration
     * @param analyzedSources analyzed sources
     * @return detected version
     * @throws IllegalArgumentException if it could not detect the version
     */
    public String detectVersion(final ProjectKeeperConfig config, final List<AnalyzedSource> analyzedSources) {
        if (config.getVersionConfig() != null) {
            final VersionProviderVisitor visitor = new VersionProviderVisitor(analyzedSources);
            config.getVersionConfig().accept(visitor);
            return visitor.getVersion();
        } else if (analyzedSources.size() == 1 && analyzedSources.get(0).getVersion() != null) {
            return analyzedSources.get(0).getVersion();
        } else {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-116").message(FAILED_TO_DETECT_VERSION
                    + " Project-keeper can only auto-detect the project version for projects with exactly one source.")
                    .mitigation("Please configure how to detect the version in your project-keeper config.")
                    .toString());
        }
    }

    @RequiredArgsConstructor
    private static class VersionProviderVisitor implements ProjectKeeperConfig.VersionConfig.Visitor {
        private final List<AnalyzedSource> analyzedSources;
        @Getter
        private String version;

        @Override
        public void visit(final ProjectKeeperConfig.FixedVersion fixedVersion) {
            this.version = fixedVersion.getVersion();
        }

        @Override
        public void visit(final ProjectKeeperConfig.VersionFromSource versionFromMavenSource) {
            final Path requestedPath = versionFromMavenSource.getPathToPom();
            this.version = this.analyzedSources.stream()//
                    .filter(source -> source.getPath().normalize().equals(requestedPath.normalize()))//
                    .findAny().orElseThrow(() -> getNoSourceFoundException(requestedPath, this.analyzedSources))//
                    .getVersion();
            if (this.version == null) {
                throw getSourceHasNoVersionException(requestedPath);
            }
        }

        private IllegalArgumentException getSourceHasNoVersionException(final Path requestedPath) {
            return new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-115")
                    .message(FAILED_TO_DETECT_VERSION
                            + " The specified source with path {{path}} did not provide a version.", requestedPath)
                    .mitigation(
                            "Please specify a different source to read from or set an explicit version in your project-keeper config..")
                    .toString());
        }

        private IllegalArgumentException getNoSourceFoundException(final Path requestedPath,
                final List<AnalyzedSource> analyzedSources) {
            final List<Path> knownSources = analyzedSources.stream().map(AnalyzedSource::getPath)
                    .collect(Collectors.toList());
            return new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-114")
                    .message(FAILED_TO_DETECT_VERSION + " Could not find a source with specified path {{path}}.",
                            requestedPath.toString())
                    .mitigation(
                            "Please make sure that you defined a source with exactly the same path. The following sources are defined in the config: {{sources}}.",
                            knownSources)
                    .toString());
        }
    }
}
