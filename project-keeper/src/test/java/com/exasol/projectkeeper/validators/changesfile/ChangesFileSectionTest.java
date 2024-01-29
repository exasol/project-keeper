package com.exasol.projectkeeper.validators.changesfile;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ChangesFileSectionTest {

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(ChangesFileSection.class).verify();
    }
}
