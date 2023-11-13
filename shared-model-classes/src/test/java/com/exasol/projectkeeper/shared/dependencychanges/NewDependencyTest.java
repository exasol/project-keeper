package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class NewDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(NewDependency.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(NewDependency.class).verify();
    }
}
