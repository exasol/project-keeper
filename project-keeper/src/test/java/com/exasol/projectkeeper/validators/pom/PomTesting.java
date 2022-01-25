package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

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

    public static Path writeResourceToTempFile(final File tempDir, final String resourceName) throws IOException {
        final Path pomFile = File.createTempFile("pom", ".xml", tempDir).toPath();
        try (final InputStream pomStream = PomTesting.class.getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(Objects.requireNonNull(pomStream), pomFile, StandardCopyOption.REPLACE_EXISTING);
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
        public List<ValidationFinding> validate() {
            final var findings = new ArrayList<ValidationFinding>();
            this.validator.validate(this.pom, this.enabledModules, findings::add);
            return findings;
        }
    }
}
