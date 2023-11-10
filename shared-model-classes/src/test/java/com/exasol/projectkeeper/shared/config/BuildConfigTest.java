package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class BuildConfigTest {

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(BuildConfig.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(BuildConfig.class).verify();
    }
}
