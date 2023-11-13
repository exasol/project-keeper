package com.exasol.projectkeeper.shared.config;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.Objects;

/**
 * CI build configuration.
 */
public final class BuildConfig {
    private static final String DEFAULT_RUNNER_OS = "ubuntu-latest";

    private final String runnerOs;
    private final boolean freeDiskSpace;
    private final List<String> exasolDbVersions;

    private BuildConfig(final Builder builder) {
        this.runnerOs = Objects.requireNonNull(builder.runnerOs, "runnerOs");
        this.freeDiskSpace = builder.freeDiskSpace;
        this.exasolDbVersions = unmodifiableList(Objects.requireNonNull(builder.exasolDbVersions, "exasolDbVersions"));
    }

    /** @return CI build runner operating system */
    public String getRunnerOs() {
        return this.runnerOs;
    }

    /** @return {@code true} if disk space should be freed before running the build */
    public boolean shouldFreeDiskSpace() {
        return freeDiskSpace;
    }

    /**
     * Exasol DB versions for which to run the CI build. If the list is empty, no Matrix build will be used.
     *
     * @return Exasol DB versions for which to run the build
     */
    public List<String> getExasolDbVersions() {
        return exasolDbVersions;
    }

    /**
     * Creates builder to build {@link BuildConfig}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link BuildConfig}.
     */
    public static final class Builder {
        private List<String> exasolDbVersions = emptyList();
        private boolean freeDiskSpace = false;
        private String runnerOs = DEFAULT_RUNNER_OS;

        private Builder() {
        }

        /**
         * CI build runner operating system, e.g. {@code ubuntu-latest}.
         *
         * @param runnerOs runner operating system
         * @return {@code this} for fluent programming
         */
        public Builder runnerOs(final String runnerOs) {
            if (runnerOs != null) {
                this.runnerOs = runnerOs;
            }
            return this;
        }

        /**
         * Some builds need more disk space, e.g. for Docker images. If this is {@code true}, the CI build will free up
         * disk space before running the actual build. This will slow down the build about one minute.
         *
         * @param freeDiskSpace {@code true} if the CI build should free up disk space
         * @return {@code this} for fluent programming
         */
        public Builder freeDiskSpace(final boolean freeDiskSpace) {
            this.freeDiskSpace = freeDiskSpace;
            return this;
        }

        /**
         * Run the CI build as a matrix build using the given Exasol DB versions. Sonar will only be run using the first
         * version in the list. If the list is empty, no Matrix build will be used.
         *
         * @param exasolDbVersions Exasol DB versions for which to run the build
         * @return {@code this} for fluent programming
         */
        public Builder exasolDbVersions(final List<String> exasolDbVersions) {
            if (exasolDbVersions != null) {
                this.exasolDbVersions = exasolDbVersions;
            }
            return this;
        }

        /**
         * Build a new {@link BuildConfig}.
         *
         * @return built class
         */
        public BuildConfig build() {
            return new BuildConfig(this);
        }
    }

    @Override
    public String toString() {
        return "BuildConfig [runnerOs=" + runnerOs + ", freeDiskSpace=" + freeDiskSpace + ",exasolDbVersions="
                + exasolDbVersions + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(runnerOs, freeDiskSpace, exasolDbVersions);
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
        final BuildConfig other = (BuildConfig) obj;
        return Objects.equals(runnerOs, other.runnerOs) && freeDiskSpace == other.freeDiskSpace
                && Objects.equals(exasolDbVersions, other.exasolDbVersions);
    }
}
