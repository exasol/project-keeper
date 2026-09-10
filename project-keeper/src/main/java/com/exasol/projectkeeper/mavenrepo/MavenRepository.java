package com.exasol.projectkeeper.mavenrepo;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static java.util.Comparator.comparing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.stream.IntStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class allows getting the latest version of project-keeper from Maven Central.
 */
// [impl->dsn~verify-own-version~2]
public class MavenRepository {

    /**
     * Get repo for CLI artifacts.
     *
     * @return Maven repository using URL for cli artifacts of project-keeper.
     */
    public static MavenRepository projectKeeperCli() {
        return of(PROJECT_KEEPER_PREFIX + "cli");
    }

    /**
     * Get repo for Maven plugins.
     *
     * @return Maven repository using URL for project-keeper maven-plugin.
     */
    public static MavenRepository projectKeeperMavenPlugin() {
        return of(PROJECT_KEEPER_PREFIX + "maven-plugin");
    }

    /**
     * Get repo for the given URL infix.
     *
     * @param urlInfix infix for URL to maven artifact, e.g. "com/exasol/project-keeper-maven-plugin"
     * @return new instance of {@link MavenRepository} for this artifact
     */
    public static MavenRepository of(final String urlInfix) {
        return new MavenRepository(BASE_URL + urlInfix + METADATA_FILE);
    }

    /**
     * Get the greatest stable version from Maven metadata.
     *
     * @param document XML DOM document to retrieve stable versions from
     * @return greatest stable version
     * @throws XmlContentException if the metadata does not contain a stable version
     */
    static String getLatestStableVersion(final Document document) throws XmlContentException {
        final Node versions = runXPath(document, VERSIONS_XPATH);
        if (versions == null) {
            throw noStableVersionException();
        }
        return IntStream.range(0, versions.getChildNodes().getLength())
                .mapToObj(versions.getChildNodes()::item)
                .filter(node -> "version".equals(node.getNodeName()))
                .map(Node::getTextContent)
                .filter(MavenRepository::isStableVersion)
                .max(comparing(Version::parse))
                .orElseThrow(MavenRepository::noStableVersionException);
    }

    static final String BASE_URL = "https://repo1.maven.org/maven2/";
    static final String METADATA_FILE = "/maven-metadata.xml";
    private static final String PROJECT_KEEPER_PREFIX = "com/exasol/project-keeper-";

    // sonar requests to get this URI from a customizable parameter which is inappropriate in the current situation
    @SuppressWarnings("java:S1075")
    private static final String LATEST_VERSION_XPATH = "/metadata/versioning/latest";
    private static final String VERSIONS_XPATH = "/metadata/versioning/versions";
    private final String url;

    /**
     * Productive code is expected to use the static methods {@link MavenRepository#projectKeeperCli()} and
     * {@link MavenRepository#projectKeeperMavenPlugin()}.
     *
     * <p>
     * This constructor is designated for tests, but as some tests are in a different package the visibility needs to be
     * public.
     * </p>
     *
     * @param url URL of maven repository.
     */
    public MavenRepository(final String url) {
        this.url = url;
    }

    /**
     * Get the greatest stable version of the artifact addressed by this repository.
     *
     * @return greatest stable version
     * @throws IllegalStateException if Maven metadata cannot be read or does not contain a stable version
     */
    public String getLatestStableVersion() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            final DocumentBuilder db = factory.newDocumentBuilder();
            try (InputStream stream = URI.create(this.url).toURL().openStream()) {
                return getLatestStableVersion(db.parse(stream));
            }
        } catch (final ParserConfigurationException | SAXException | IOException | XmlContentException exception) {
            throw new IllegalStateException("Couldn't get latest stable version from " + this.url, exception);
        }
    }

    private static boolean isStableVersion(final String version) {
        return Version.PATTERN.matcher(version).matches();
    }

    private static XmlContentException noStableVersionException() {
        return new XmlContentException("Couldn't find a stable version in node " + VERSIONS_XPATH);
    }

    /**
     * This exception is thrown if response of maven repository in json format does not contain the expected keys.
     */
    public static class XmlContentException extends Exception {
        private static final long serialVersionUID = 1L;

        /**
         * Create a new instance.
         *
         * @param message the detail message.
         */
        public XmlContentException(final String message) {
            super(message);
        }
    }

    /**
     * Get the URL.
     *
     * @return URL of this Maven repository
     */
    public String getUrl() {
        return this.url;
    }
}
