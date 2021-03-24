package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;

/**
 * Maven Dependency.
 */
public class Dependency {
    private final String name;
    private final String url;
    private final List<License> licenses;
    private final Scope scope;

    /**
     * Create a new instance of {@link Dependency}.
     * 
     * @param name     dependency name
     * @param url      dependency url
     * @param licenses list of licenses
     * @param scope    scope
     */
    public Dependency(final String name, final String url, final List<License> licenses, final Scope scope) {
        this.name = name;
        this.url = url;
        this.licenses = licenses;
        this.scope = scope;
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
    public String getUrl() {
        return this.url;
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
     * Get the scope of this dependency.
     * 
     * @return dependency scope
     */
    public Scope getScope() {
        return this.scope;
    }

    /**
     * Scope of a {@link Dependency}.
     */
    public enum Scope {
        COMPILE, RUNTIME, TEST, PLUGIN
    }
}
