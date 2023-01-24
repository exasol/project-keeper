package com.exasol.projectkeeper.validators.pom.io;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.mavenrepo.MavenRepository;
import com.exasol.projectkeeper.mavenrepo.MavenRepository.XmlContentException;

/**
 * This class updates the versions of the plugins in the maven templates. Simply run it as Java application.
 */
public class TemplateUpdater {

    private static final String PLUGIN = "/plugin/";
    private static final String VERSION_XPATH = PLUGIN + "version";
    private static final Logger LOGGER = Logger.getLogger(TemplateUpdater.class.getName());

    public static void main(final String... args) {
        new TemplateUpdater().update(Paths.get("src/main/resources/maven_templates"));
    }

    private final PomFileWriter pomFileWriter;
    private final PomFileReader pomFileReader;

    TemplateUpdater() throws IllegalStateException {
        final Transformer transformer = PomFileWriter.createTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        this.pomFileWriter = new PomFileWriter(transformer);
        this.pomFileReader = PomFileReader.instance();
    }

    private void update(final Path path) {
        LOGGER.info("Updating templates in folder " + path);
        try {
            Files.list(path).forEach(this::process);
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private void process(final Path file) {
        if (!file.toString().endsWith(".xml")) {
            return;
        }
        final Document pom = this.pomFileReader.parseFile(file);
        final String version = getText(pom, VERSION_XPATH);
        final String latest = getLatestVersion(pom);
        if ((version == null) || !version.equals(latest)) {
            LOGGER.info("- " + file.getFileName() + ": Updating version " + version + " -> " + latest);
            runXPath(pom, VERSION_XPATH).setTextContent(latest);
            this.pomFileWriter.writeToFile(pom, file);
        }
    }

    private String getLatestVersion(final Document pom) {
        final String group = getText(pom, PLUGIN + "groupId");
        final String artifact = getText(pom, PLUGIN + "artifactId");
        try {
            return new MavenRepository(url(group, artifact)).getLatestVersion();
        } catch (ParserConfigurationException | SAXException | IOException | XmlContentException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String url(final String group, final String artifact) {
        final List<String> list = Arrays.asList(group.split("."));
        Collections.reverse(list);
        return MavenRepository.url(String.join("/", list) + "/" + artifact);
    }

    private String getText(final Node node, final String xpath) {
        return runXPath(node, xpath).getTextContent();
    }
}
