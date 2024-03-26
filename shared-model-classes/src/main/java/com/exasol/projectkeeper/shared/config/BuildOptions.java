package com.exasol.projectkeeper.shared.config;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.*;

/**
 * CI build configuration.
 */
public final class BuildOptions {
    private static final String DEFAULT_RUNNER_OS = "ubuntu-latest";

    private final String runnerOs;
    private final boolean freeDiskSpace;
    private final List<String> exasolDbVersions;
    private final List<WorkflowStep> setupSteps;
    private final WorkflowStep buildStep;
    private final List<WorkflowStep> cleanupSteps;

    private BuildOptions(final Builder builder) {
        this.runnerOs = Objects.requireNonNull(builder.runnerOs, "runnerOs");
        this.freeDiskSpace = builder.freeDiskSpace;
        this.exasolDbVersions = unmodifiableList(Objects.requireNonNull(builder.exasolDbVersions, "exasolDbVersions"));
        this.setupSteps = unmodifiableList(Objects.requireNonNull(builder.setupSteps, "setupSteps"));
        this.buildStep = builder.buildStep;
        this.cleanupSteps = unmodifiableList(Objects.requireNonNull(builder.cleanupSteps, "cleanupSteps"));
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
     * Get the optional custom setup steps that should be executed before the build in the GitHub workflow.
     * 
     * @return custom setup steps
     */
    public List<WorkflowStep> getSetupSteps() {
        return setupSteps;
    }

    /**
     * Get the optional custom build step that should be executed instead of the default step in the GitHub workflow.
     * 
     * @return custom build step
     */
    public Optional<WorkflowStep> getBuildStep() {
        return Optional.ofNullable(buildStep);
    }

    /**
     * Get the optional custom cleanup steps that should be executed after the build in the GitHub workflow.
     * 
     * @return custom cleanup steps
     */
    public List<WorkflowStep> getCleanupSteps() {
        return cleanupSteps;
    }

    /**
     * Creates builder to build {@link BuildOptions}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link BuildOptions}.
     */
    public static final class Builder {
        private List<String> exasolDbVersions = emptyList();
        private boolean freeDiskSpace = false;
        private String runnerOs = DEFAULT_RUNNER_OS;
        private List<WorkflowStep> setupSteps = emptyList();
        private WorkflowStep buildStep = null;
        private List<WorkflowStep> cleanupSteps = emptyList();

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
         * Add custom setup steps that should be executed before the build in the GitHub workflow.
         * 
         * @param setupSteps custom setup steps
         * @return {@code this} for fluent programming
         */
        public Builder setupSteps(final List<WorkflowStep> setupSteps) {
            if (setupSteps != null) {
                this.setupSteps = setupSteps;
            }
            return this;
        }

        /**
         * Add a custom build step that should be executed instead of the default step in the GitHub workflow.
         * 
         * @param buildStep custom build step
         * @return {@code this} for fluent programming
         */
        public Builder buildStep(final WorkflowStep buildStep) {
            this.buildStep = buildStep;
            return this;
        }

        /**
         * Add custom cleanup steps that should be executed after the build in the GitHub workflow.
         * 
         * @param cleanupSteps custom cleanup steps
         * @return {@code this} for fluent programming
         */
        public Builder cleanupSteps(final List<WorkflowStep> cleanupSteps) {
            if (cleanupSteps != null) {
                this.cleanupSteps = cleanupSteps;
            }
            return this;
        }

        /**
         * Build a new {@link BuildOptions}.
         *
         * @return built class
         */
        public BuildOptions build() {
            return new BuildOptions(this);
        }
    }

    @Override
    public String toString() {
        return "BuildOptions [runnerOs=" + runnerOs + ", freeDiskSpace=" + freeDiskSpace + ", exasolDbVersions="
                + exasolDbVersions + ", setupSteps=" + setupSteps + ", buildStep=" + buildStep + ", cleanupSteps="
                + cleanupSteps + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(runnerOs, freeDiskSpace, exasolDbVersions, setupSteps, buildStep, cleanupSteps);
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
        final BuildOptions other = (BuildOptions) obj;
        return Objects.equals(runnerOs, other.runnerOs) && freeDiskSpace == other.freeDiskSpace
                && Objects.equals(exasolDbVersions, other.exasolDbVersions)
                && Objects.equals(setupSteps, other.setupSteps) && Objects.equals(buildStep, other.buildStep)
                && Objects.equals(cleanupSteps, other.cleanupSteps);
    }
}
