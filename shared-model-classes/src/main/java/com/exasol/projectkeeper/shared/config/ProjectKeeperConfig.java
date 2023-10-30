package com.exasol.projectkeeper.shared.config;

import java.util.*;

/**
 * This class represents the project-keeper configuration.
 * <p>
 * Use {@link ProjectKeeperConfig#builder()} for creating new instances.
 */
public final class ProjectKeeperConfig {
    private static final String DEFAULT_CI_BUILD_RUNNER_OS = "ubuntu-latest";

    private final List<Source> sources;
    private final List<String> linkReplacements;
    // [impl->dsn~excluding~1]
    private final List<String> excludes;
    private final VersionConfig versionConfig;
    private final String ciBuildRunnerOS;

    private ProjectKeeperConfig(final ProjectKeeperConfigBuilder builder) {
        this.sources = builder.sources;
        this.linkReplacements = builder.linkReplacements;
        this.excludes = builder.excludes;
        this.versionConfig = builder.versionConfig;
        this.ciBuildRunnerOS = builder.ciBuildRunnerOS;
    }

    /** @return List with source-projects to crawl */
    public List<Source> getSources() {
        return sources;
    }

    /** @return List of replacements for broken links */
    public List<String> getLinkReplacements() {
        return linkReplacements;
    }

    /** @return List of regular expressions that match validation messages to exclude */
    public List<String> getExcludes() {
        return excludes;
    }

    /** @return project version configuration */
    public VersionConfig getVersionConfig() {
        return versionConfig;
    }

    /** @return CI build runner operating system */
    public String getCiBuildRunnerOS() {
        return ciBuildRunnerOS;
    }

    /** @return a new builder for creating {@link ProjectKeeperConfig} instances */
    public static ProjectKeeperConfig.ProjectKeeperConfigBuilder builder() {
        return new ProjectKeeperConfig.ProjectKeeperConfigBuilder();
    }

    /**
     * Builder for creating {@link ProjectKeeperConfig} instances.
     */
    public static class ProjectKeeperConfigBuilder {
        private List<Source> sources = Collections.emptyList();
        private List<String> linkReplacements = Collections.emptyList();
        private List<String> excludes = Collections.emptyList();
        private VersionConfig versionConfig;
        private String ciBuildRunnerOS = DEFAULT_CI_BUILD_RUNNER_OS;

        private ProjectKeeperConfigBuilder() {
            // empty by intention
        }

        /**
         * @param sources List with source-projects to crawl
         * @return {@code this}.
         */
        public ProjectKeeperConfig.ProjectKeeperConfigBuilder sources(final List<Source> sources) {
            this.sources = sources;
            return this;
        }

        /**
         * @param linkReplacements List of replacements for broken links
         * @return {@code this}.
         */
        public ProjectKeeperConfig.ProjectKeeperConfigBuilder linkReplacements(final List<String> linkReplacements) {
            this.linkReplacements = linkReplacements;
            return this;
        }

        /**
         * @param excludes List of regular expressions that match validation messages to exclude
         * @return {@code this}.
         */
        public ProjectKeeperConfig.ProjectKeeperConfigBuilder excludes(final List<String> excludes) {
            this.excludes = excludes;
            return this;
        }

        /**
         * @param versionConfig project version configuration
         * @return {@code this}.
         */
        public ProjectKeeperConfig.ProjectKeeperConfigBuilder versionConfig(final VersionConfig versionConfig) {
            this.versionConfig = versionConfig;
            return this;
        }

        /**
         * @param ciBuildRunnerOS operating system for CI builds (default: {@code ubuntu-latest})
         * @return {@code this}.
         */
        public ProjectKeeperConfig.ProjectKeeperConfigBuilder ciBuildRunnerOS(final String ciBuildRunnerOS) {
            if (ciBuildRunnerOS != null) {
                this.ciBuildRunnerOS = ciBuildRunnerOS;
            }
            return this;
        }

        /** @return a new instance */
        public ProjectKeeperConfig build() {
            return new ProjectKeeperConfig(this);
        }
    }

    @Override
    public String toString() {
        return "ProjectKeeperConfig [sources=" + sources + ", linkReplacements=" + linkReplacements + ", excludes="
                + excludes + ", versionConfig=" + versionConfig + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sources, linkReplacements, excludes, versionConfig, ciBuildRunnerOS);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectKeeperConfig other = (ProjectKeeperConfig) obj;
        return Objects.equals(sources, other.sources) && Objects.equals(linkReplacements, other.linkReplacements)
                && Objects.equals(excludes, other.excludes) && Objects.equals(versionConfig, other.versionConfig)
                && Objects.equals(ciBuildRunnerOS, other.ciBuildRunnerOS);
    }
}
