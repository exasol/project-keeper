package com.exasol.projectkeeper.shared.dependencies;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProjectDependenciesTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectDependencies.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(ProjectDependencies.class).verify();
    }
}
