package com.exasol.projectkeeper.shared.config;

import java.util.Objects;

/**
 * CI build configuration.
 */
public final class BuildConfig {
    private static final String DEFAULT_RUNNER_OS = "ubuntu-latest";

    private final String runnerOs;

    private BuildConfig(final Builder builder) {
        this.runnerOs = Objects.requireNonNull(builder.runnerOs, "runnerOs");
    }

    /** @return CI build runner operating system */
    public String getRunnerOs() {
        return this.runnerOs;
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
        return "BuildConfig [runnerOs=" + runnerOs + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(runnerOs);
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
        return Objects.equals(runnerOs, other.runnerOs);
    }
}
