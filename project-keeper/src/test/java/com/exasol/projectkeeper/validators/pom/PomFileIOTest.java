package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.test.TestMavenModel;

class PomFileIOTest {
    private static final String TEST_ELEMENT = "my-test-element";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
    }

    @Test
    void testRead() {
        assertThat(new PomFileIO().parsePomFile(this.tempDir.resolve("pom.xml")), hasXPath("/project"));
    }

    @Test
    void testReadFromString() throws IOException {
        final String pomContent = Files.readString(this.tempDir.resolve("pom.xml"));
        assertThat(new PomFileIO().parsePomFile(pomContent), hasXPath("/project"));
    }

    @Test
    void testWrite() {
        final Path pomFile = this.tempDir.resolve("pom.xml");
        final Document document = new PomFileIO().parsePomFile(pomFile);
        final Node root = runXPath(document, "/project");
        root.appendChild(document.createElement(TEST_ELEMENT));
        new PomFileIO().writePomFile(document, pomFile);
        assertThat(new PomFileIO().parsePomFile(pomFile), hasXPath("/project/" + TEST_ELEMENT));
    }
}