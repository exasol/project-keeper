package com.exasol.projectkeeper.validators.changesfile;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class NamedDependencyChangeReportTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(NamedDependencyChangeReport.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(NamedDependencyChangeReport.class).verify();
    }
}
