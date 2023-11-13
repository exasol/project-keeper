package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class RemovedDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(RemovedDependency.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(RemovedDependency.class).verify();
    }
}
