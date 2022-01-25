package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.writeResourceToTempFile;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Node;

class PomFileIOTest {

    public static final String TEST_ELEMENT = "my-test-element";

    @Test
    void testRead(@TempDir final File tempDir) throws IOException {
        final Path pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileIO pomFileIO = new PomFileIO(pomFile);
        assertThat(pomFileIO.getContent(), hasXPath("/project"));
    }

    @Test
    void testWrite(@TempDir final File tempDir) throws IOException {
        final Path pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileIO pomFileIO = new PomFileIO(pomFile);
        final Node root = runXPath(pomFileIO.getContent(), "/project");
        root.appendChild(pomFileIO.getContent().createElement(TEST_ELEMENT));
        pomFileIO.writeChanges();
        final PomFileIO pomFileIO2 = new PomFileIO(pomFile);
        assertThat(pomFileIO2.getContent(), hasXPath("/project/" + TEST_ELEMENT));
    }
}