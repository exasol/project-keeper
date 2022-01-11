package com.exasol.projectkeeper.validators.changesfile;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class TemporaryPomFileTest {

    @Test
    void testCreate() throws IOException {
        try (final TemporaryPomFile pomFile = new TemporaryPomFile("test")) {
            assertThat(Files.readString(pomFile.getPomFile()), equalTo("test"));
        }
    }

    @Test
    void testDelete() throws IOException {
        final TemporaryPomFile pomFile = new TemporaryPomFile("test");
        pomFile.close();
        assertFalse(Files.exists(pomFile.getPomFile()));
    }
}