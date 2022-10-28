package com.exasol.projectkeeper.validators.pom;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements access to a pom file.
 */
class PomFileIO {

    private final Map<String, String> properties = new HashMap<>();

    PomFileIO() {
    }

    PomFileIO property(final String key, final String value) {
        this.properties.put(key, value);
        return this;
    }

    /**
     * Write a pom file content into a file.
     *
     * @param document        content
     * @param destinationFile file to write to
     */
    public void writePomFile(final Document document, final Path destinationFile) {
        try (final FileOutputStream outputStream = new FileOutputStream(destinationFile.toFile())) {
            writePomFile(document, outputStream);
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("F-PK-CORE-100")
                            .message("Failed to write xml (pom) to file {file}.", destinationFile).toString(),
                    exception);
        }
    }

    public String writePomFileToString(final Document document) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            writePomFile(document, outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("F-PK-CORE-106")
                    .message("Failed to convert xml (pom) to string.").toString(), exception);
        }
    }

    private void writePomFile(final Document document, final OutputStream outputStream) {
        try {
            final var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final var transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            for (final Entry<String, String> e : this.properties.entrySet()) {
                transformer.setOutputProperty(e.getKey(), e.getValue());
            }
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            final var domSource = new DOMSource(document);
            final var streamResult = new StreamResult(outputStream);
            transformer.transform(domSource, streamResult);
        } catch (final TransformerException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-12").message("Failed to replace pom-file.").toString(),
                    exception);
        }
    }

    /*
     * Trim the whitespace from the given node and its children. <p> Inspired by https://stackoverflow.com/a/33564346
     * </p>
     */
    public static void trimWhitespace(final Node node) {
        final NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); ++index) {
            final Node child = children.item(index);
            if (child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            trimWhitespace(child);
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
                    ExaError.messageBuilder("E-PK-CORE-108").message("Failed to read pom file from string.").toString(),
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

    /**
     * Read a pom file as XML document.
     *
     * @param pomFile pom file
     * @return xml document
     */
    public Document parsePomFile(final Path pomFile) {
        try (final InputStream pomFileStream = new FileInputStream(pomFile.toFile())) {
            final Document document = parsePomFile(pomFileStream);
            trimWhitespace(document);// otherwise, the formatter in output adds new-lines
            return document;
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-107")
                    .message("Failed to read pom file {{path}}.", pomFile).toString(), exception);
        }
    }
}
