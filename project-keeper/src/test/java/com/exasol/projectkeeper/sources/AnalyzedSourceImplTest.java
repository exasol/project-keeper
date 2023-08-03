package com.exasol.projectkeeper.sources;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AnalyzedSourceImplTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(AnalyzedSourceImpl.class).verify();
    }
}
