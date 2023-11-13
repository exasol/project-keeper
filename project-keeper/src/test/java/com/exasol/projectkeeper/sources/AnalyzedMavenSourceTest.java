package com.exasol.projectkeeper.sources;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedMavenSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(AnalyzedMavenSource.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AnalyzedMavenSource.class).verify();
    }
}
