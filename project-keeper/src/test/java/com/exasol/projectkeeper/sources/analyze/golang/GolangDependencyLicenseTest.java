package com.exasol.projectkeeper.sources.analyze.golang;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class GolangDependencyLicenseTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(GolangDependencyLicense.class).verify();
    }
}
