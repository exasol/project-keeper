package com.exasol.projectkeeper.validators.pom.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.test.TestMavenModel;

class PomFileReaderTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
    }

    @Test
    void parseFile() {
        assertThat(PomFileReader.parse(this.tempDir.resolve("pom.xml")), hasXPath("/project"));
    }

    @Test
    void parseString() throws IOException {
        final String pomContent = Files.readString(this.tempDir.resolve("pom.xml"));
        assertThat(PomFileReader.parse(pomContent), hasXPath("/project"));
    }

    @Test
    void failFile() throws IOException {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> PomFileReader.parse(this.tempDir.resolve("abc.xml")));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-107: Failed to read pom file "));
    }

    @Test
    void failString() throws IOException {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> PomFileReader.parse("blabla"));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-108: Failed to read pom from string 'blabla'."));
    }
}