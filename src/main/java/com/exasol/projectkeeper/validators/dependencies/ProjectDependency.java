package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;

/**
 * Maven dependency.
 */
public class ProjectDependency {
    private final String name;
    private final String websiteUrl;
    private final List<License> licenses;
    private final Type type;

    /**
     * Create a new instance of {@link ProjectDependency}.
     * 
     * @param name       dependency name
     * @param websiteUrl dependency website
     * @param licenses   list of licenses
     * @param type       dependency type
     */
    public ProjectDependency(final String name, final String websiteUrl, final List<License> licenses,
            final Type type) {
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.licenses = licenses;
        this.type = type;
    }

    /**
     * Get the name of the dependency.
     * 
     * @return dependency name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the URL of the dependency.
     * 
     * @return URL of the dependency
     */
    public String getWebsiteUrl() {
        return this.websiteUrl;
    }

    /**
     * Get the licenses.
     * 
     * @return list of licenses
     */
    public List<License> getLicenses() {
        return this.licenses;
    }

    /**
     * Get the type of this dependency.
     * 
     * @return dependency type
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Type of a {@link ProjectDependency}.
     */
    public enum Type {
        /** Compile dependency. */
        COMPILE,
        /** Runtime dependency */
        RUNTIME,
        /** Test dependency */
        TEST,
        /** Plugin */
        PLUGIN
    }
}
