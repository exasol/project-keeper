package com.exasol.projectkeeper.validators.dependencies;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProjectWithDependenciesTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ProjectWithDependencies.class).verify();
    }
}
