package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class FixedVersionTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FixedVersion.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(FixedVersion.class).verify();
    }
}
