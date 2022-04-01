package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class NewDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(NewDependency.class).verify();
    }
}
