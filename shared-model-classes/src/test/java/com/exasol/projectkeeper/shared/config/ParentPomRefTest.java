package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ParentPomRefTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ParentPomRef.class).verify();
    }
}
