package com.exasol.projectkeeper.validators.changesfile;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ChangesFileNameTest {
    @Test
    void equalsContractFilename() {
        EqualsVerifier.forClass(ChangesFileName.class).verify();
    }
}
