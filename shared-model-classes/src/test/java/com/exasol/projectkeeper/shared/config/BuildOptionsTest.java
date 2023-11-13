package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class BuildOptionsTest {

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(BuildOptions.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(BuildOptions.class).verify();
    }
}
