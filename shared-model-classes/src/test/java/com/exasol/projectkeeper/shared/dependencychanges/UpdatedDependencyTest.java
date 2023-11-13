package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class UpdatedDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(UpdatedDependency.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(UpdatedDependency.class).verify();
    }
}
