package com.exasol.projectkeeper.shared.config;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class WorkflowStepTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(WorkflowStep.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(WorkflowStep.class).verify();
    }
}
