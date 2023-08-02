package com.exasol.projectkeeper.shared.dependencies;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProjectDependenciesTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ProjectDependencies.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
