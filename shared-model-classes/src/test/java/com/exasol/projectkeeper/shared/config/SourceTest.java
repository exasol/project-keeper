package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class SourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Source.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(Source.class).verify();
    }
}
