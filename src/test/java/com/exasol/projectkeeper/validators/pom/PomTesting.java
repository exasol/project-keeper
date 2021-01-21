package com.exasol.projectkeeper.validators.pom;

import static com.exasol.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.*;

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

    public static PomValidationRunner validationOf(final PomValidator validator, final Document pom,
            final List<ProjectKeeperModule> enabledModules) {
        return new PomValidationRunner(validator, pom, enabledModules);
    }

    private static class PomValidationRunner implements Validator {
        private final PomValidator validator;
        private final Document pom;
        private final List<ProjectKeeperModule> enabledModules;

        private PomValidationRunner(final PomValidator validator, final Document pom,
                final List<ProjectKeeperModule> enabledModules) {
            this.validator = validator;
            this.pom = pom;
            this.enabledModules = enabledModules;
        }

        @Override
        public Validator validate(final Consumer<ValidationFinding> findingConsumer) {
            this.validator.validate(this.pom, this.enabledModules, findingConsumer);
            return this;
        }
    }
}
