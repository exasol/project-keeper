package com.exasol.projectkeeper.validators.pom.io;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.test.TestMavenModel;

class PomFileWriterTest {
    private static final String TEST_ELEMENT = "my-test-element";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        new TestMavenModel().writeAsPomToProject(this.tempDir);
    }

    @Test
    void writeFile() {
        final Path pomFile = this.tempDir.resolve("sample.xml");
        PomFileWriter.writeFile(document(), pomFile);
        assertThat(PomFileReader.parse(pomFile), hasXPath("/project/" + TEST_ELEMENT));
    }

    @Test
    void writeString() {
        final String string = PomFileWriter.writeString(document());
        assertThat(PomFileReader.parse(string), hasXPath("/project/" + TEST_ELEMENT));
    }

    @Test
    void failFile() {
        final Path pomFile = this.tempDir.resolve("non/existing/sample.xml");
        final Exception exception = assertThrows(IllegalStateException.class,
                () -> PomFileWriter.writeFile(document(), pomFile));
        assertThat(exception.getMessage(), startsWith("F-PK-CORE-100: Failed to write POM XML to file "));
    }

    @Test
    void failString() throws TransformerException {
        final Transformer transformer = mock(Transformer.class);
        doThrow(new TransformerException("")).when(transformer).transform(any(), any());
        final PomFileWriter testee = new PomFileWriter(transformer);
        final Exception exception = assertThrows(IllegalStateException.class,
                () -> testee.writeToString(document()));
        assertThat(exception.getMessage(), equalTo("F-PK-CORE-106: Failed to convert POM XML document to string."));
    }

    private Document document() {
        final Path pomFile = this.tempDir.resolve("pom.xml");
        final Document document = PomFileReader.parse(pomFile);
        final Node root = runXPath(document, "/project");
        root.appendChild(document.createElement(TEST_ELEMENT));
        return document;
    }
}