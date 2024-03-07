package com.exasol.projectkeeper.validators.changesfile;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class FixedIssueTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(FixedIssue.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(FixedIssue.class).verify();
    }
}
