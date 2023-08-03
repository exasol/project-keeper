package com.exasol.projectkeeper.validators.files;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class FileTemplateFromResourceTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(FileTemplateFromResource.class).verify();
    }
}
