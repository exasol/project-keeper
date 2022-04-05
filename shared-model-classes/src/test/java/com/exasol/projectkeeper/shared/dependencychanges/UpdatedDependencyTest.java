package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class UpdatedDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(UpdatedDependency.class).verify();
    }
}
