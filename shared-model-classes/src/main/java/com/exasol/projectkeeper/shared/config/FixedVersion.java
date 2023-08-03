package com.exasol.projectkeeper.shared.config;

import lombok.Data;

/**
 * Config for a provided version string.
 */
@Data
public final class FixedVersion implements VersionConfig {
    private final String version;

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}