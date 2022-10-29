package com.exasol.projectkeeper.validators.pom;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.mavenrepo.MavenRepository;
import com.exasol.projectkeeper.mavenrepo.MavenRepository.JsonContentException;

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

    private final PomFileIO pomFileIo;

    TemplateUpdater() {
        this.pomFileIo = new PomFileIO().property(OutputKeys.OMIT_XML_DECLARATION, "yes");
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
        final Document pom = this.pomFileIo.parsePomFile(file);
        final String version = getText(pom, VERSION_XPATH);
        final String latest = getLatestVersion(pom);
        if ((version == null) || !version.equals(latest)) {
            LOGGER.info("- " + file.getFileName() + ": Updating version " + version + " -> " + latest);
            runXPath(pom, VERSION_XPATH).setTextContent(latest);
            final Path out = file.getParent().resolve(file.getFileName() + ".out");
            this.pomFileIo.writePomFile(pom, out);
        }
    }

    private String getLatestVersion(final Document pom) {
        final String group = getText(pom, PLUGIN + "groupId");
        final String artifact = getText(pom, PLUGIN + "artifactId");
        try {
            return new MavenRepository(MavenRepository.url(group, artifact)).getLatestVersion();
        } catch (IOException | JsonContentException exception) {
            throw new IllegalStateException(exception);
        }
    }

    private String getText(final Node node, final String xpath) {
        return runXPath(node, xpath).getTextContent();
    }
}
