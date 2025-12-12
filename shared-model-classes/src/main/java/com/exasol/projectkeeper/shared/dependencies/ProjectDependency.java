package com.exasol.projectkeeper.shared.dependencies;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic dependency.
 * 
 * @param type        type of the dependency
 * @param name        human readable name of the module of the dependency, e.g. {@code Jakarta JSON Processing API}
 * @param coordinates coordinates of the dependency, e.g. {@code jakarta.json:jakarta.json-api} for Maven dependencies
 * @param websiteUrl  URL of the website for the dependency
 * @param licenses    list of licenses associated with the dependency
 */
public final record ProjectDependency(Type type, String name, String coordinates, String websiteUrl,
        List<License> licenses) implements BaseDependency {

    /**
     * Create a new builder.
     * 
     * @return Builder for new instances of {@link ProjectDependency}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Get type.
     * 
     * @return type of the dependency
     */
    @Override
    public Type getType() {
        return this.type;
    }

    /**
     * Get name.
     * 
     * @return name of the module of the dependency
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get website URL.
     * 
     * @return URL of the website associated with the current project dependency
     */
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    /**
     * Get licenses.
     * 
     * @return list of licenses associated with the current project dependency
     */
    public List<License> getLicenses() {
        return this.licenses;
    }

    public Builder copy() {
        return builder()
                .type(this.type)
                .name(this.name)
                .coordinates(this.coordinates)
                .websiteUrl(this.websiteUrl)
                .licenses(this.licenses);
    }

    @Override
    public String toString() {
        return this.type + " dependency '" + this.name + "', (" + this.coordinates + "), " + this.websiteUrl + ", "
                + this.licenses.size()
                + " licenses: " + this.licenses.stream().map(License::toString).collect(Collectors.joining(", "));
    }

    /** Builder for a new instance of {@link ProjectDependency} */
    public static final class Builder {

        private Type type;
        private String name;
        private String coordinates;
        private String websiteUrl;
        private List<License> licenses;

        private Builder() {

        }

        /**
         * Set type.
         * 
         * @param type type of the dependency to build
         * @return this for fluent programming
         */
        public Builder type(final Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set name.
         * 
         * @param name name of the dependency to build
         * @return this for fluent programming
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set coordinates.
         * 
         * @param coordinates coordinates of the dependency to build
         * @return this for fluent programming
         */
        public Builder coordinates(final String coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        /**
         * Set website URL.
         * 
         * @param websiteUrl URL of the website for the dependency to build
         * @return this for fluent programming
         */
        public Builder websiteUrl(final String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        /**
         * Set licenses.
         * 
         * @param licenses list of licenses to associate with the dependency to build
         * @return this for fluent programming
         */
        public Builder licenses(final List<License> licenses) {
            this.licenses = licenses;
            return this;
        }

        /**
         * Build a new instance.
         * 
         * @return new instance of {@link ProjectDependency}
         */
        public ProjectDependency build() {
            return new ProjectDependency(this.type, this.name, this.coordinates, this.websiteUrl, this.licenses);
        }
    }
}
