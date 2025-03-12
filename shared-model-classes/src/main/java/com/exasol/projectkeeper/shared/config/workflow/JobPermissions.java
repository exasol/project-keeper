package com.exasol.projectkeeper.shared.config.workflow;

import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.util.*;
import java.util.stream.Stream;

/**
 * GitHub permissions for a job, e.g. {@code content: read}. See <a href=
 * "https://docs.github.com/en/actions/writing-workflows/choosing-what-your-workflow-does/controlling-permissions-for-github_token">GitHub
 * documentation</a>.
 */
public final class JobPermissions {

    public static final JobPermissions DEFAULT = builder().add("content", AccessLevel.READ).build();

    private final Map<String, AccessLevel> permissions;

    private JobPermissions(final Builder builder) {
        this.permissions = requireNonNull(builder.permissions);
    }

    public Map<String, AccessLevel> getPermissions() {
        return unmodifiableMap(permissions);
    }

    /**
     * Create a new builder for {@link JobPermissions}.
     * 
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "JobPermissions [permissions=" + permissions + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissions);
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
        final JobPermissions other = (JobPermissions) obj;
        return Objects.equals(permissions, other.permissions);
    }

    /**
     * Builder for {@link JobPermissions}.
     */
    public static class Builder {
        public Map<String, AccessLevel> permissions = new HashMap<>();

        private Builder() {
        }

        /**
         * Add a new permission
         * 
         * @param permission  permission, e.g. {@code content} or {@code packages}
         * @param accessLevel access level
         * @return {@code this} for fluent programming
         */
        public Builder add(final String permission, final AccessLevel accessLevel) {
            this.permissions.put(permission, accessLevel);
            return this;
        }

        /**
         * Build a new {@link JobPermissions} object.
         * 
         * @return new permissions
         */
        public JobPermissions build() {
            return new JobPermissions(this);
        }
    }

    /**
     * Access level for permissions.
     */
    public enum AccessLevel {
        NONE("none"), READ("read"), WRITE("write");

        private final String name;

        private AccessLevel(final String name) {
            this.name = name;
        }

        /**
         * Get the name of this access level.
         * 
         * @return name
         */
        public String getName() {
            return name;
        }

        public static Optional<AccessLevel> forName(final String name) {
            return Stream.of(values()).filter(level -> level.getName().equals(name)).findFirst();
        }
    }
}
