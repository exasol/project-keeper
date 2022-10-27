package com.exasol.projectkeeper.shared.dependencychanges;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class DependencyChangeReportTest {

    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(DependencyChangeReport.class).verify();
    }
}
