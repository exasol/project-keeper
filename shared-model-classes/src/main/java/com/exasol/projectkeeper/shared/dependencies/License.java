package com.exasol.projectkeeper.shared.dependencies;

import java.util.Objects;

/**
 * License.
 */
public final class License {
    /** Name of the license */
    private String name;
    /** Link to the license */
    private String url;

    /**
     * Create a new license.
     * <p>
     * Default constructor required for JSON deserializing.
     */
    public License() {
        this(null, null);
    }

    /**
     * Create a new license.
     * 
     * @param name license name, e.g. {@code MIT}
     * @param url  license URL, e.g. {@code https://opensource.org/licenses/MIT}
     */
    public License(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Get the name of the license, e.g. {@code MIT}.
     * 
     * @return license name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the link to the license, e.g. {@code https://opensource.org/licenses/MIT}.
     * 
     * @return license link
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the license name.
     * <p>
     * Required for JSON deserializing.
     * 
     * @param name license name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Set the license URL.
     * <p>
     * Required for JSON deserializing.
     * 
     * @param url license URL.
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.name + " " + this.url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
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
        final License other = (License) obj;
        return Objects.equals(name, other.name) && Objects.equals(url, other.url);
    }
}
