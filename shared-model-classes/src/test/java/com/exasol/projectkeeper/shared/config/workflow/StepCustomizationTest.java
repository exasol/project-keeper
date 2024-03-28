package com.exasol.projectkeeper.shared.config.workflow;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class StepCustomizationTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(StepCustomization.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(StepCustomization.class).verify();
    }
}
