package com.exasol.projectkeeper.sources;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedMavenSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(AnalyzedMavenSource.class).verify();
    }
}