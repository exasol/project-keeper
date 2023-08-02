package com.exasol.projectkeeper.validators.changesfile;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class NamedDependencyChangeReportTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(NamedDependencyChangeReport.class).verify();
    }
}
