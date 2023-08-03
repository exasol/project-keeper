package com.exasol.projectkeeper.sources;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedSourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.simple().forClass(AnalyzedSourceImpl.class).verify();
    }
}
