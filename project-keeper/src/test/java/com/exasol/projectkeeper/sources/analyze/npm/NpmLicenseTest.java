package com.exasol.projectkeeper.sources.analyze.npm;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class NpmLicenseTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(NpmLicense.class).verify();
    }
}
