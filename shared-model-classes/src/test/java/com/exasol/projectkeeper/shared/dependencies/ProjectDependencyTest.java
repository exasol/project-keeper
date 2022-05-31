package com.exasol.projectkeeper.shared.dependencies;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProjectDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectDependency.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
