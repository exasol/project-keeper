package com.exasol.projectkeeper.shared.dependencies;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic dependency.
 */
public final class ProjectDependency extends BaseDependency {

    public static Builder builder() {
        return new Builder();
    }

    /** Website url */
    private String websiteUrl;
    /** License */
    private List<License> licenses = new ArrayList<>();

    ProjectDependency() {
    }

    /**
     * @return URL of the website associated with the current project dependency
     */
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    /**
     * @return list of licenses associated with the current project dependency
     */
    public List<License> getLicenses() {
        return this.licenses;
    }

    /**
     * Builder for a new instance of {@link ProjectDependency}
     */
    public static final class Builder {
        private final ProjectDependency projectDependency = new ProjectDependency();

        /**
         * @param type type of the dependency to build
         * @return this for fluent programming
         */
        public Builder type(final Type type) {
            this.projectDependency.type = type;
            return this;
        }

        /**
         *
         * @param name name of the dependency to build
         * @return this for fluent programming
         */
        public Builder name(final String name) {
            this.projectDependency.name = name;
            return this;
        }

        /**
         *
         * @param websiteUrl URL of the website for the dependency to build
         * @return this for fluent programming
         */
        public Builder websiteUrl(final String websiteUrl) {
            this.projectDependency.websiteUrl = websiteUrl;
            return this;
        }

        /**
         *
         * @param licenses list of licenses to associate with the dependency to build
         * @return this for fluent programming
         */
        public Builder licenses(final List<License> licenses) {
            this.projectDependency.licenses = licenses;
            return this;
        }

        public ProjectDependency build() {
            return this.projectDependency;
        }
    }
}
