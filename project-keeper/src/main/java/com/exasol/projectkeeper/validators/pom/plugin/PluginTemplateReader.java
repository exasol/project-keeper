package com.exasol.projectkeeper.validators.pom.plugin;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads a maven plugin configuration from resources.
 */
public class PluginTemplateReader {
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = createXmlDocumentBuilderFactory();

    PluginTemplateReader() {
    }

    private static DocumentBuilderFactory createXmlDocumentBuilderFactory() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return factory;
    }

    /**
     * Read a maven plugin configuration from resources.
     * 
     * @param templateResourceName resource file name
     * @return XML object
     */
    public Node readPluginTemplate(final String templateResourceName) {
        try (final InputStream templateInputStream = readResource(templateResourceName)) {
            final DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(templateInputStream).getFirstChild();
        } catch (final IOException | SAXException | ParserConfigurationException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-10")
                    .message("Failed to parse plugin template {{template}}.", templateResourceName).ticketMitigation()
                    .toString(), exception);
        }
    }

    private InputStream readResource(final String templateResourceName) {
        final InputStream stream = getClass().getClassLoader().getResourceAsStream(templateResourceName);
        if (stream == null) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-11")
                    .message("Failed to open plugins template {{template}}.", templateResourceName).ticketMitigation()
                    .toString());
        }
        return stream;
    }
}
