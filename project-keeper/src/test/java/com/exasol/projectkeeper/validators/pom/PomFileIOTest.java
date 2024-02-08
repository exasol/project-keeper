package com.exasol.projectkeeper.validators.pom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PomFileIOTest {

    @TempDir
    Path tempDir;

    @Test
    void write() throws IOException {
        final Path file = tempDir.resolve("pom.xml");
        final Model model = new Model();
        model.setArtifactId("dummy-artifact");
        testee().writePom(model, file);
        assertThat(Files.readString(file), allOf(startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
                containsString("<artifactId>dummy-artifact</artifactId>"), endsWith("</project>\n")));
    }

    @Test
    void writeFails() throws IOException {
        final Path file = tempDir.resolve("non-existing-dir").resolve("pom.xml");
        final Model model = new Model();
        final PomFileIO testee = testee();
        final UncheckedIOException exception = assertThrows(UncheckedIOException.class,
                () -> testee.writePom(model, file));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-173: Failed to write pom"));
    }

    @Test
    void read() throws IOException {
        final Path file = Path.of("pom.xml");
        final Model pom = testee().readPom(file);
        assertThat(pom.getArtifactId(), equalTo("project-keeper-core"));
    }

    @Test
    void readFails() throws IOException {
        final Path file = tempDir.resolve("pom.xml");
        final PomFileIO testee = testee();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> testee.readPom(file));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-172: Failed to read pom"));
    }

    private PomFileIO testee() {
        return new PomFileIO();
    }

}
