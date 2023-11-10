package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class VersionFromSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(VersionFromSource.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(VersionFromSource.class).verify();
    }
}
