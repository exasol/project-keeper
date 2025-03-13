package com.exasol.projectkeeper.validators.pom.io;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

    private static final Logger LOGGER = Logger.getLogger(TemplateUpdater.class.getName());

    public static void main(final String... args) {
        new TemplateUpdater() //
                .updateTemplates(findTemplatePath()) //
                .updatePomFile(Path.of("pom.xml"));
    }

    private static Path findTemplatePath() {
        return List
                .of(Path.of("project-keeper/src/main/resources/maven_templates"),
                        Path.of("src/main/resources/maven_templates"))
                .stream() //
                .filter(Files::isDirectory) //
                .map(Path::toAbsolutePath) //
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Template directory not found. Adapt working directory"));
    }

    private final PomFileWriter pomFileWriter;
    private final PomFileReader pomFileReader;

    TemplateUpdater() throws IllegalStateException {
        final Transformer transformer = PomFileWriter.createTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        this.pomFileWriter = new PomFileWriter(transformer);
        this.pomFileReader = PomFileReader.instance();
    }

    private TemplateUpdater updateTemplates(final Path dir) {
        LOGGER.info("Updating templates in folder " + dir);
        try {
            Files.list(dir) //
                    .filter(p -> p.toString().endsWith(".xml")) //
                    .forEach(this::updateSingleTemplate);
            return this;
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private void updateSingleTemplate(final Path file) {
        final Reporter logger = (group, artifact, version, latest) -> LOGGER.info( //
                "- " + file.getFileName() + ": Updating version " + version + " -> " + latest);
        final Document pom = this.pomFileReader.parseFile(file);
        final Node plugin = runXPath(pom, "/plugin");
        if (updatePlugin(plugin, logger)) {
            this.pomFileWriter.writeToFile(pom, file);
        }
    }

    private boolean updatePlugin(final Node plugin, final Reporter logger) {
        final Node versionNode = runXPath(plugin, "version");
        if (versionNode == null) {
            return false;
        }
        final String version = versionNode.getTextContent();
        final String group = getText(plugin, "groupId");
        final String artifact = getText(plugin, "artifactId");
        final String latest = getLatestVersion(group, artifact);
        if ((version != null) && version.equals(latest)) {
            return false;
        }
        logger.report(group, artifact, version, latest);
        versionNode.setTextContent(latest);
        return true;
    }

    private TemplateUpdater updatePomFile(final Path file) {
        final Reporter logger = (group, artifact, version, latest) -> LOGGER.info( //
                "- " + file.getFileName() + ": " //
                        + "Updating version " + "of " + group + ":" + artifact //
                        + " from " + version + " -> " + latest);
        final Document pom = this.pomFileReader.parseFile(file);
        final Node parent = runXPath(pom, "/project/build/plugins");
        if (parent == null) {
            return this;
        }
        Node plugin = parent.getFirstChild();
        boolean changed = false;
        while (plugin != null) {
            changed |= updatePlugin(plugin, logger);
            plugin = plugin.getNextSibling();
        }
        if (changed) {
            LOGGER.info("  - writing file " + file.toAbsolutePath());
            this.pomFileWriter.writeToFile(pom, file);
        }
        return this;
    }

    private String getLatestVersion(final String group, final String artifact) {
        try {
            return MavenRepository.of(url(group, artifact)).getLatestVersion();
        } catch (ParserConfigurationException | SAXException | IOException | XmlContentException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String url(final String group, final String artifact) {
        return group.replace(".", "/") + "/" + artifact;
    }

    private String getText(final Node node, final String xpath) {
        return runXPath(node, xpath).getTextContent();
    }

    @FunctionalInterface
    interface Reporter {
        void report(final String group, final String artifact, final String version, final String latest);
    }
}
