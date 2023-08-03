package com.exasol.projectkeeper.validators.finding;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ValidationFindingGroupTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ValidationFindingGroup.class).verify();
    }
}
