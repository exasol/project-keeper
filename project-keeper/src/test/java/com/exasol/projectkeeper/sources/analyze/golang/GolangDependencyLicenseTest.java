package com.exasol.projectkeeper.sources.analyze.golang;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class GolangDependencyLicenseTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(GolangDependencyLicense.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GolangDependencyLicense.class).verify();
    }
}
