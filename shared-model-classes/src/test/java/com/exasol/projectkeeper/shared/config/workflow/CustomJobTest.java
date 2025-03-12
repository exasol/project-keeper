package com.exasol.projectkeeper.shared.config.workflow;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class CustomJobTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(CustomJob.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(CustomJob.class).verify();
    }
}
