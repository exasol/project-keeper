package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;

/**
 * Config that introduces PK to read the version from a source.
 * 
 * @param pathToPom path to POM file
 */
public record VersionFromSource(Path pathToPom) implements VersionConfig {

    /**
     * Get path to POM file.
     * 
     * @return path to POM file
     */
    public Path getPathToPom() {
        return pathToPom;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
