package com.exasol.projectkeeper.validators.pom;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.exasol.errorreporting.ExaError;

/**
 * This class renders a XML document as string. That's useful for debugging and testing.
 */
public class PomRenderer {

    private PomRenderer() {
        // empty on purpose
    }

    /**
     * Render a XML document as string
     * 
     * @param xmlDocument XML document to render
     * @return XML string
     */
    public static String renderPom(final Node xmlDocument) {
        try {
            final var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final Transformer transformer = transformerFactory.newTransformer();
            final var domSource = new DOMSource(xmlDocument);
            final ByteArrayOutputStream targetStream = new ByteArrayOutputStream();
            final var streamResult = new StreamResult(targetStream);
            transformer.transform(domSource, streamResult);
            return targetStream.toString(StandardCharsets.UTF_8);
        } catch (final TransformerException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-74").message("Failed to render XML document.").toString(), exception);
        }
    }
}
