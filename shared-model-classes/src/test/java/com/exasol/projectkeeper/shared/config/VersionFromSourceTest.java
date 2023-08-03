package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class VersionFromSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(VersionFromSource.class).verify();
    }
}
