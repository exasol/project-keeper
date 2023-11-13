package com.exasol.projectkeeper.shared.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class LicenseTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(License.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    void testToString() {
        assertThat(new License("name", "url"), hasToString("name url"));
    }
}
