package com.exasol.projectkeeper.validators.finding;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ValidationFindingGroupTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ValidationFindingGroup.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ValidationFindingGroup.class).verify();
    }
}
