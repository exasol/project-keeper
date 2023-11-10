package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ParentPomRefTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ParentPomRef.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ParentPomRef.class).verify();
    }
}
