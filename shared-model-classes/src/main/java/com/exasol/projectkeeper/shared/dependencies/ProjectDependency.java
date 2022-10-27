package com.exasol.projectkeeper.shared.dependencies;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generic dependency.
 */
public final class ProjectDependency implements BaseDependency {

    /**
     * @return Builder for new instances of {@link ProjectDependency}.
     */
    public static Builder builder() {
        return new Builder();
    }

    private Type type;
    private String name;
    private String websiteUrl;
    private List<License> licenses = new ArrayList<>();

    /**
     * Create an empty project dependency
     */
    public ProjectDependency() {
    }

    /**
     * Create a new instance of {@link ProjectDependency} with all fields defined
     *
     * @param type       type of the dependency
     * @param name       name of the module of the dependency
     * @param websiteUrl URL of the website associated with the current project dependency
     * @param licenses   list of licenses associated with the current project dependency
     */
    public ProjectDependency(final Type type, final String name, final String websiteUrl,
            final List<License> licenses) {
        this.type = type;
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.licenses = licenses;
    }

    /**
     * @return type of the dependency
     */
    @Override
    public Type getType() {
        return this.type;
    }

    /**
     * @return name of the module of the dependency
     */
    @Override
    public String getName() {
        return this.name;
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
     * Only needed for serialization and deserialization.
     *
     * @param type type of the dependency
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * Only needed for serialization and deserialization.
     *
     * @param name name of the module of the dependency
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Only needed for serialization and deserialization.
     *
     * @param websiteUrl URL of the website associated with the current project dependency
     */
    public void setWebsiteUrl(final String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    /**
     * Only needed for serialization and deserialization.
     *
     * @param licenses list of licenses associated with the current project dependency
     */
    public void setLicenses(final List<License> licenses) {
        this.licenses = licenses;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.licenses, this.name, this.type, this.websiteUrl);
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
        final ProjectDependency other = (ProjectDependency) obj;
        return Objects.equals(this.licenses, other.licenses) && Objects.equals(this.name, other.name)
                && (this.type == other.type) && Objects.equals(this.websiteUrl, other.websiteUrl);
    }

    @Override
    public String toString() {
        return this.type + " dependency '" + this.name + "', " + this.websiteUrl + ", " + this.licenses.size()
                + " licenses: " + this.licenses.stream().map(License::toString).collect(Collectors.joining(", "));
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
         * @param name name of the dependency to build
         * @return this for fluent programming
         */
        public Builder name(final String name) {
            this.projectDependency.name = name;
            return this;
        }

        /**
         * @param websiteUrl URL of the website for the dependency to build
         * @return this for fluent programming
         */
        public Builder websiteUrl(final String websiteUrl) {
            this.projectDependency.websiteUrl = websiteUrl;
            return this;
        }

        /**
         * @param licenses list of licenses to associate with the dependency to build
         * @return this for fluent programming
         */
        public Builder licenses(final List<License> licenses) {
            this.projectDependency.licenses = licenses;
            return this;
        }

        /**
         * @return new instance of {@link ProjectDependency}
         */
        public ProjectDependency build() {
            return this.projectDependency;
        }
    }
}
