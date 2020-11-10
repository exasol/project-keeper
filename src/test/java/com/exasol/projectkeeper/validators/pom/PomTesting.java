package com.exasol.projectkeeper.validators.pom;

import static com.exasol.xpath.XPathErrorHanlingWrapper.runXPath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class PomTesting {
    public static final String POM_WITH_NO_PLUGINS = "pomWithNoPlugins.xml";

    public static Node invalidatePom(final String removeXpath, final Node validPom)
            throws IOException, SAXException, ParserConfigurationException {
        final Node plugin = runXPath(validPom, "/project/build/plugins/plugin");
        final Node nodeToRemove = runXPath(plugin, removeXpath);
        nodeToRemove.getParentNode().removeChild(nodeToRemove);
        return plugin;
    }

    public static Document readXmlFromResources(final String resourceName)
            throws IOException, SAXException, ParserConfigurationException {
        try (final InputStream pomFileStream = PomTesting.class.getClassLoader().getResourceAsStream(resourceName)) {
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(pomFileStream);
        }
    }

    public static File writeResourceToTempFile(final File tempDir, final String resourceName) throws IOException {
        final File pomFile = File.createTempFile("pom", ".xml", tempDir);
        try (final InputStream pomStream = PomTesting.class.getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(Objects.requireNonNull(pomStream), pomFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return pomFile;
    }
}
