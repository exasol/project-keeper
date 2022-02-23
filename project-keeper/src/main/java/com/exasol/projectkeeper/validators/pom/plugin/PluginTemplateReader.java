package com.exasol.projectkeeper.validators.pom.plugin;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.errorreporting.ExaError;

/**
 * This class reads a maven plugin configuration from resources.
 */
public class PluginTemplateReader {
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    /**
     * Read a maven plugin configuration from resources.
     * 
     * @param templateResourceName resource file name
     * @return XML object
     */
    public Node readPluginTemplate(final String templateResourceName) {
        try (final var templateInputStream = getClass().getClassLoader().getResourceAsStream(templateResourceName)) {
            if (templateInputStream == null) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-11")
                        .message("Failed to open plugins template {{template}}.", templateResourceName)
                        .ticketMitigation().toString());
            }
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            DOCUMENT_BUILDER_FACTORY.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final var documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(templateInputStream).getFirstChild();
        } catch (final IOException | SAXException | ParserConfigurationException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-10")
                    .message("Failed to parse plugin template {{template}}.", templateResourceName).ticketMitigation()
                    .toString(), exception);
        }
    }
}
