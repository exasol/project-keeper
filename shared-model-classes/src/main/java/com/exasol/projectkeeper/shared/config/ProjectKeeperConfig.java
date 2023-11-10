package com.exasol.projectkeeper.shared.config;

import java.util.*;

/**
 * This class represents the project-keeper configuration.
 * <p>
 * Use {@link ProjectKeeperConfig#builder()} for creating new instances.
 */
public final class ProjectKeeperConfig {

    private final List<Source> sources;
    private final List<String> linkReplacements;
    // [impl->dsn~excluding~1]
    private final List<String> excludes;
    private final VersionConfig versionConfig;
    private final BuildConfig buildConfig;

    private ProjectKeeperConfig(final Builder builder) {
        this.sources = Objects.requireNonNull(builder.sources, "sources");
        this.linkReplacements = Objects.requireNonNull(builder.linkReplacements, "linkReplacements");
        this.excludes = Objects.requireNonNull(builder.excludes, "excludes");
        this.versionConfig = builder.versionConfig;
        this.buildConfig = Objects.requireNonNull(builder.buildConfig, "buildConfig");
    }

    /** @return List with source-projects to crawl */
    public List<Source> getSources() {
        return this.sources;
    }

    /** @return List of replacements for broken links */
    public List<String> getLinkReplacements() {
        return this.linkReplacements;
    }

    /** @return List of regular expressions that match validation messages to exclude */
    public List<String> getExcludes() {
        return this.excludes;
    }

    /** @return project version configuration */
    public VersionConfig getVersionConfig() {
        return this.versionConfig;
    }

    /** @return CI build configuration */
    public BuildConfig getCiBuildConfig() {
        return this.buildConfig;
    }

    /** @return a new builder for creating {@link ProjectKeeperConfig} instances */
    public static ProjectKeeperConfig.Builder builder() {
        return new ProjectKeeperConfig.Builder();
    }

    /**
     * Builder for creating {@link ProjectKeeperConfig} instances.
     */
    public static class Builder {
        private List<Source> sources = Collections.emptyList();
        private List<String> linkReplacements = Collections.emptyList();
        private List<String> excludes = Collections.emptyList();
        private VersionConfig versionConfig;
        private BuildConfig buildConfig = BuildConfig.builder().build();

        private Builder() {
            // empty by intention
        }

        /**
         * @param sources List with source-projects to crawl
         * @return {@code this}.
         */
        public ProjectKeeperConfig.Builder sources(final List<Source> sources) {
            this.sources = sources;
            return this;
        }

        /**
         * @param linkReplacements List of replacements for broken links
         * @return {@code this}.
         */
        public ProjectKeeperConfig.Builder linkReplacements(final List<String> linkReplacements) {
            this.linkReplacements = linkReplacements;
            return this;
        }

        /**
         * @param excludes List of regular expressions that match validation messages to exclude
         * @return {@code this}.
         */
        public ProjectKeeperConfig.Builder excludes(final List<String> excludes) {
            this.excludes = excludes;
            return this;
        }

        /**
         * @param versionConfig project version configuration
         * @return {@code this}.
         */
        public ProjectKeeperConfig.Builder versionConfig(final VersionConfig versionConfig) {
            this.versionConfig = versionConfig;
            return this;
        }

        /**
         * @param buildConfig project build configuration
         * @return {@code this}.
         */
        public ProjectKeeperConfig.Builder buildConfig(final BuildConfig buildConfig) {
            if (buildConfig != null) {
                this.buildConfig = buildConfig;
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
        return "ProjectKeeperConfig [sources=" + this.sources + ", linkReplacements=" + this.linkReplacements
                + ", excludes=" + this.excludes + ", versionConfig=" + this.versionConfig + ", buildConfig="
                + this.buildConfig + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sources, this.linkReplacements, this.excludes, this.versionConfig, this.buildConfig);
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
        return Objects.equals(this.sources, other.sources)
                && Objects.equals(this.linkReplacements, other.linkReplacements)
                && Objects.equals(this.excludes, other.excludes)
                && Objects.equals(this.versionConfig, other.versionConfig)
                && Objects.equals(this.buildConfig, other.buildConfig);
    }
}
