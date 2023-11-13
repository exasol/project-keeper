package com.exasol.projectkeeper;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class RepoInfoTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(RepoInfo.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(RepoInfo.class).verify();
    }
}
