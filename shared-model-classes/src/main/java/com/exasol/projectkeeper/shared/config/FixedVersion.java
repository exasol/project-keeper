package com.exasol.projectkeeper.shared.config;

/**
 * Config for a provided version string.
 * 
 * @param version fixed version number
 */
public record FixedVersion(String version) implements VersionConfig {

    /**
     * Get version.
     * 
     * @return fixed version number
     */
    public String getVersion() {
        return version;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
