package com.exasol.projectkeeper.shared.config.workflow;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * This configuration class allows customizing a job inside a {@link CustomWorkflow}.
 */
public final class CustomJob {
    private final String jobName;
    private final JobPermissions permissions;

    private CustomJob(final Builder builder) {
        this.jobName = requireNonNull(builder.jobName, "jobName");
        this.permissions = requireNonNull(builder.permissions, "permissions");
    }

    /**
     * Get the job name, e.g. {@code build-and-test}.
     * 
     * @return job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Get the job permissions.
     * 
     * @return permissions
     */
    public JobPermissions getPermissions() {
        return permissions;
    }

    /**
     * Create a new builder for {@code CustomJob}.
     * 
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "CustomJob [jobName=" + jobName + ", permissions=" + permissions + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName, permissions);
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
        final CustomJob other = (CustomJob) obj;
        return Objects.equals(jobName, other.jobName) && Objects.equals(permissions, other.permissions);
    }

    /**
     * Builder for {@code CustomJob}.
     */
    public static class Builder {
        private String jobName;
        private JobPermissions permissions = JobPermissions.DEFAULT;

        private Builder() {
        }

        /**
         * Set the job name, e.g. {@code build-and-test}.
         * 
         * @param jobName job name
         * @return {@code this} for fluent programming
         */
        public Builder jobName(final String jobName) {
            this.jobName = requireNonNull(jobName);
            return this;
        }

        /**
         * Set permissions.
         * 
         * @param permissions permissions
         * @return {@code this} for fluent programming
         */
        public Builder permissions(final JobPermissions permissions) {
            this.permissions = requireNonNull(permissions);
            return this;
        }

        /**
         * Build a new {@link CustomJob}.
         * 
         * @return new job
         */
        public CustomJob build() {
            return new CustomJob(this);
        }
    }
}
