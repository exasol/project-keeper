package com.exasol.projectkeeper.sources;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedSourceImplTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(AnalyzedSourceImpl.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(AnalyzedSourceImpl.class).verify();
    }
}
