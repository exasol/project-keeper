package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.XPathErrorHanlingWrapper.runXpath;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class PomTesting {

    public static Node invalidatePom(final String removeXpath, final String pomResourceName)
            throws IOException, SAXException, ParserConfigurationException {
        final Document pom = getParsedPom(pomResourceName);
        final Node plugin = runXpath(pom, "/project/build/plugins/plugin[artifactId/text() = 'versions-maven-plugin']");
        final Node nodeToRemove = runXpath(plugin, removeXpath);
        nodeToRemove.getParentNode().removeChild(nodeToRemove);
        return plugin;
    }

    public static Document getParsedPom(final String resourceName)
            throws IOException, SAXException, ParserConfigurationException {
        try (final InputStream pomFileStream = PomTesting.class.getClassLoader().getResourceAsStream(resourceName)) {
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(pomFileStream);
        }
    }
}
