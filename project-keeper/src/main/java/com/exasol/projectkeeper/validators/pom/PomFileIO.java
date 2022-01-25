package com.exasol.projectkeeper.validators.pom;

import java.io.*;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements access to a pom file.
 */
class PomFileIO {
    private final Path pomFile;
    private final Document content;

    /**
     * Create a new instance of {@link PomFileIO}.
     * 
     * @param pomFile pom file to wrap.
     */
    public PomFileIO(final Path pomFile) {
        this.pomFile = pomFile;
        this.content = parsePomFile();
    }

    /**
     * Get the content of the pom file.
     * 
     * @return content of the pom file
     */
    Document getContent() {
        return this.content;
    }

    /**
     * Write the changes that were made on the content back to the file.
     * <p>
     * The {@link Document} is mutable. So you can change it for example using {@link Document#appendChild(Node)}.
     * </p>
     */
    public void writeChanges() {
        try {
            final var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final var transformer = transformerFactory.newTransformer();
            final var domSource = new DOMSource(this.content);
            final var streamResult = new StreamResult(this.pomFile.toFile());
            transformer.transform(domSource, streamResult);
        } catch (final TransformerException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-12").message("Failed to replace pom-file.").toString(),
                    exception);
        }
    }

    private Document parsePomFile() {
        try (final InputStream pomFileStream = new FileInputStream(this.pomFile.toFile())) {
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
