package com.exasol.projectkeeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Runner for {@link PomTemplate}.
 */
public class PomFileTemplateRunner {

    public static final Collection<PomTemplate> TEMPLATES = List.of(new VersionMavenPluginPomTemplate());
    private final Document pom;
    private final File pomFile;

    /**
     * Create a new instance of {@link PomFileTemplateRunner}.
     * 
     * @param pomFile pom file to create the runner for.
     */
    public PomFileTemplateRunner(final File pomFile) {
        this.pomFile = pomFile;
        try (final InputStream pomFileStream = new FileInputStream(pomFile)) {
            this.pom = parsePomFile(pomFileStream);
        } catch (final IOException exception) {
            throw new IllegalStateException("E-PK-8 Failed to open pom.xml.", exception);
        }
    }

    /**
     * Verify the pom file.
     * 
     * @param log            logger
     * @param enabledModules list of enabled modules
     * @return {@code true}, if verification was successful.
     */
    boolean verify(final Log log, final List<String> enabledModules) {
        return run(log, enabledModules, PomTemplate.RunMode.VERIFY);
    }

    /**
     * Fix the pom file.
     * 
     * @param log            logger
     * @param enabledModules list of enabled modules
     */
    void fix(final Log log, final List<String> enabledModules) {
        run(log, enabledModules, PomTemplate.RunMode.FIX);
        writePomFile();
    }

    private void writePomFile() {
        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMSource domSource = new DOMSource(this.pom);
            final StreamResult streamResult = new StreamResult(this.pomFile);
            transformer.transform(domSource, streamResult);
        } catch (final TransformerException exception) {
            throw new IllegalStateException("E-PK-12 Failed to replace pom-file. ", exception);
        }
    }

    private boolean run(final Log log, final List<String> enabledModules, final PomTemplate.RunMode runMode) {
        boolean success = true;
        for (final PomTemplate template : TEMPLATES) {
            if (enabledModules.contains(template.getModule())) {
                try {
                    template.run(this.pom, runMode);
                } catch (final PomTemplateValidationException exception) {
                    success = false;
                    log.error(exception.getMessage());
                }
            }
        }
        return success;
    }

    private Document parsePomFile(final InputStream pomFileStream) {
        try {
            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(pomFileStream);
        } catch (final ParserConfigurationException | IOException | SAXException exception) {
            throw new IllegalStateException("E-PK-7 Failed to parse pom.xml.", exception);
        }
    }
}
