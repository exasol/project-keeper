package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class FixedVersionTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FixedVersion.class).verify();
    }
}
