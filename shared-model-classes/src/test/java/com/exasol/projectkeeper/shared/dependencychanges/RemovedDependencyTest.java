package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RemovedDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(RemovedDependency.class).verify();
    }
}
