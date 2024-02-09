package com.exasol.projectkeeper.sources.analyze.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProcessResultTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProcessResult.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ProcessResult.class).verify();
    }
}
