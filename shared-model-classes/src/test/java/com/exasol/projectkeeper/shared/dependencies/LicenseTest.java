package com.exasol.projectkeeper.shared.dependencies;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class LicenseTest {
    @Test
    void equalsContract() {
        EqualsVerifier.forClass(License.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
