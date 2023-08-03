package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class DependencyChangeReportTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.simple().forClass(DependencyChangeReport.class).verify();
    }
}
