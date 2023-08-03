package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Config that introduces PK to read the version from a source.
 */
public final class VersionFromSource implements VersionConfig {
    private final Path pathToPom;

    /**
     * Create a mew instance.
     * 
     * @param pathToPom path to POM file
     */
    public VersionFromSource(final Path pathToPom) {
        this.pathToPom = pathToPom;
    }

    /** @return path to POM file */
    public Path getPathToPom() {
        return pathToPom;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "VersionFromSource [pathToPom=" + pathToPom + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathToPom);
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
        final VersionFromSource other = (VersionFromSource) obj;
        return Objects.equals(pathToPom, other.pathToPom);
    }
}