package com.exasol.projectkeeper.validators.pom.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.exasol.errorreporting.ExaError;

/**
 * This class implements write access to a pom file.
 */
public class PomFileWriter {

    /**
     * Write a pom file document content into a file.
     *
     * @param document        pom file content
     * @param destinationFile file to write to
     */
    public static void writeFile(final Document document, final Path destinationFile) {
        instance().writeToFile(document, destinationFile);

    }

    /**
     * Write a pom file document to a string.
     *
     * @param document pom file to write
     * @return result string representation of the pom file
     */
    public static String writeString(final Document document) {
        return instance().writeToString(document);
    }

    private static PomFileWriter instance() {
        return new PomFileWriter(createTransformer());
    }

    static Transformer createTransformer() throws IllegalStateException {
        try {
            final var factory = TransformerFactory.newInstance();
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            return transformer;
        } catch (final TransformerConfigurationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private final Transformer transformer;

    PomFileWriter(final Transformer transformer) {
        this.transformer = transformer;
    }

    void writeToFile(final Document document, final Path destinationFile) {
        try (final FileOutputStream stream = new FileOutputStream(destinationFile.toFile())) {
            write(document, stream);
        } catch (final TransformerException | IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-100") //
                    .message("Failed to write POM XML to file {{file}}.", destinationFile) //
                    .toString(), exception);
        }
    }

    String writeToString(final Document document) {
        try (final ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            write(document, stream);
            return stream.toString(StandardCharsets.UTF_8);
        } catch (final TransformerException | IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-106") //
                    .message("Failed to convert POM XML document to string.") //
                    .toString(), exception);
        }
    }

    private void write(final Document document, final OutputStream stream) throws TransformerException {
        this.transformer.transform(new DOMSource(document), new StreamResult(stream));
    }
}
