package com.exasol.projectkeeper.shared.config;

import java.nio.file.Path;

import lombok.Data;

/**
 * Config that introduces PK to read the version from a source.
 */
@Data
public final class VersionFromSource implements VersionConfig {
    private final Path pathToPom;

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}