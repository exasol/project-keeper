package com.exasol.projectkeeper.validators.dependencies;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProjectWithDependenciesTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectWithDependencies.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ProjectWithDependencies.class).verify();
    }
}
