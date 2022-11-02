package com.exasol.projectkeeper.validators.pom.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements reading a POM file.
 */
public class PomFileReader {

    /**
     * Read a pom file as XML document.
     *
     * @param file pom file
     * @return {@link org.w3c.dom.Document} representing the POM file
     */
    public static Document parse(final Path file) {
        return instance().parseFile(file);
    }

    /**
     * Read a pom file content as XML document.
     *
     * @param content content of a pom.xml file
     * @return {@link org.w3c.dom.Document} representing the POM file
     */
    public static Document parse(final String content) {
        return instance().parseString(content);
    }

    static PomFileReader instance() {
        return new PomFileReader(documentBuilder());
    }

    static DocumentBuilder documentBuilder() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newDocumentBuilder();
        } catch (final IllegalArgumentException | ParserConfigurationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private final DocumentBuilder documentBuilder;

    private PomFileReader(final DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    /**
     * Trim the whitespace from the given node and its children.
     * <p>
     * Inspired by https://stackoverflow.com/a/33564346
     * </p>
     *
     * @param node XML node to be trimmed
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

    Document parseFile(final Path pomFile) {
        try (final InputStream pomFileStream = new FileInputStream(pomFile.toFile())) {
            final Document document = this.documentBuilder.parse(pomFileStream);
            trimWhitespace(document); // otherwise, the formatter adds new-lines to the output
            return document;
        } catch (final IOException | SAXException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-107") //
                    .message("Failed to read pom file {{path}}.", pomFile) //
                    .toString(), exception);
        }
    }

    private Document parseString(final String pomFileContent) {
        try (final InputStream pomFileStream = new ByteArrayInputStream(
                pomFileContent.getBytes(StandardCharsets.UTF_8))) {
            return this.documentBuilder.parse(pomFileStream);
        } catch (final IOException | SAXException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-108") //
                    .message("Failed to read pom from string {{string}}.", pomFileContent) //
                    .toString(), exception);
        }
    }
}
