package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Source.class).verify();
    }
}
