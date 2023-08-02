package com.exasol.projectkeeper;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class RepoInfoTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(RepoInfo.class).verify();
    }
}
