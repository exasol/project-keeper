package com.exasol.projectkeeper.validators.dependencies;

/**
 * License.
 */
public class License {
    private final String name;
    private final String url;

    /**
     * Create a new instance of {@link License}.
     * 
     * @param name license name
     * @param url  license url
     */
    public License(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Get the name of the license.
     * 
     * @return license name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the url of this license.
     * 
     * @return license url
     */
    public String getUrl() {
        return this.url;
    }
}
