package com.exasol.projectkeeper.validators.pom;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements access to a pom file.
 */
class PomFileIO {

    /**
     * Write a pom file content into a file.
     * 
     * @param document        content
     * @param destinationFile file to write to
     */
    public void writePomFile(final Document document, final Path destinationFile) {
        try {
            final var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final var transformer = transformerFactory.newTransformer();
            final var domSource = new DOMSource(document);
            final var streamResult = new StreamResult(destinationFile.toFile());
            transformer.transform(domSource, streamResult);
        } catch (final TransformerException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-12").message("Failed to replace pom-file.").toString(),
                    exception);
        }
    }

    /**
     * Read a pom file as XML document.
     * 
     * @param pomFile pom file
     * @return xml document
     */
    public Document parsePomFile(final Path pomFile) {
        try (final InputStream pomFileStream = new FileInputStream(pomFile.toFile())) {
            return parsePomFile(pomFileStream);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-97")
                    .message("Failed to read pom file {{path}}.", pomFile).toString(), exception);
        }
    }

    /**
     * Read a pom file content as XML document.
     *
     * @param pomFileContent content of a pom.xml file
     * @return xml document
     */
    public Document parsePomFile(final String pomFileContent) {
        try (final InputStream pomFileStream = new ByteArrayInputStream(
                pomFileContent.getBytes(StandardCharsets.UTF_8))) {
            return parsePomFile(pomFileStream);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-98").message("Failed to read pom file from string.").toString(),
                    exception);
        }
    }

    private Document parsePomFile(final InputStream pomFileStream) {
        try {
            final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(pomFileStream);
        } catch (final ParserConfigurationException | IOException | SAXException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-7").message("Failed to parse pom.xml.").toString(), exception);
        }
    }
}
