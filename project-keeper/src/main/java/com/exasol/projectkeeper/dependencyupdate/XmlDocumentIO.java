package com.exasol.projectkeeper.dependencyupdate;

import java.nio.file.Path;
import java.util.Optional;

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
    /**
     * Read an XML document from a file.
     * 
     * @param path path to the file
     * @return XML document
     */
    Document read(final Path path) {
        return PomFileReader.parse(path);
    }

    /**
     * Write an XML document to a file.
     * 
     * @param document XML document
     * @param path     path to the file
     */
    void write(final Document document, final Path path) {
        PomFileWriter.writeFile(document, path);
    }

    /**
     * Run an XPath query on an XML document and return the first result.
     * 
     * @param current current node
     * @param xPath   XPath query
     * @return first result of the query
     */
    Optional<Node> runXPath(final Node current, final String xPath) {
        return Optional.ofNullable(XPathErrorHandlingWrapper.runXPath(current, xPath));
    }
}
