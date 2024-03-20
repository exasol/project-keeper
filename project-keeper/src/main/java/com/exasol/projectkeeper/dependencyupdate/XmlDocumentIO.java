package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.validators.pom.io.PomFileReader;
import com.exasol.projectkeeper.validators.pom.io.PomFileWriter;
import com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper;

/**
 * This class provides read and write access to XML documents.
 * <p>
 * The advantage of this class is that it can be easily mocked in tests and it preserves comments when writing XML.
 */
class XmlDocumentIO {
    Document read(final Path path) {
        return PomFileReader.parse(path);
    }

    void write(final Document document, final Path path) {
        PomFileWriter.writeFile(document, path);
    }

    Node runXPath(final Node current, final String xPath) {
        return XPathErrorHandlingWrapper.runXPath(current, xPath);
    }
}
