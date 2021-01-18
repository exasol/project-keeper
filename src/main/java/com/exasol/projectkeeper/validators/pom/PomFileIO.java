package com.exasol.projectkeeper.validators.pom;

import java.io.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements access to a pom file.
 */
public class PomFileIO {
    private final File pomFile;
    private final Document content;

    /**
     * Create a new instance of {@link PomFileIO}.
     * 
     * @param pomFile pom file to wrap.
     */
    public PomFileIO(final File pomFile) {
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
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource domSource = new DOMSource(this.content);
            final StreamResult streamResult = new StreamResult(this.pomFile);
            transformer.transform(domSource, streamResult);
        } catch (final TransformerException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-12").message("Failed to replace pom-file.").toString(), exception);
        }
    }

    private Document parsePomFile() {
        try (final InputStream pomFileStream = new FileInputStream(this.pomFile)) {
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(pomFileStream);
        } catch (final ParserConfigurationException | IOException | SAXException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-7").message("Failed to parse pom.xml.").toString(), exception);
        }
    }
}
