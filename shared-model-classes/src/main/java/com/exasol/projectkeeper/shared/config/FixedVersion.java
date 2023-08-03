package com.exasol.projectkeeper.shared.config;

import java.util.Objects;

import javax.annotation.processing.Generated;

/**
 * Config for a provided version string.
 */
public final class FixedVersion implements VersionConfig {
    private final String version;

    /**
     * Create a new instance.
     * 
     * @param version fixed version number
     */
    public FixedVersion(final String version) {
        this.version = version;
    }

    /** @return fixed version number */
    public String getVersion() {
        return version;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "FixedVersion [version=" + version + "]";
    }

    @Override
    @Generated("vscode")
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    @Generated("vscode")
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
        final FixedVersion other = (FixedVersion) obj;
        return Objects.equals(version, other.version);
    }
}