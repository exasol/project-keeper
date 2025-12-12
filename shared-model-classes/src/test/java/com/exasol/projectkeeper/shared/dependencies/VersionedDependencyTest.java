package com.exasol.projectkeeper.shared.dependencies;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class VersionedDependencyTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(VersionedDependency.class).suppress(Warning.NULL_FIELDS).verify();
    }
}
