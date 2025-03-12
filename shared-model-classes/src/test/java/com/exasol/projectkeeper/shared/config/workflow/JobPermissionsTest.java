package com.exasol.projectkeeper.shared.config.workflow;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class JobPermissionsTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(JobPermissions.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(JobPermissions.class).verify();
    }
}
