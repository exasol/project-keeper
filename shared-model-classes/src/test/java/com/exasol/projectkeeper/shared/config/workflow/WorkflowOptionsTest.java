package com.exasol.projectkeeper.shared.config.workflow;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class WorkflowOptionsTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(WorkflowOptions.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(WorkflowOptions.class).verify();
    }
}
