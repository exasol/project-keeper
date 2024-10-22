package com.exasol.projectkeeper.mavenrepo;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class allows getting the latest version of project-keeper from Maven Central.
 */
// [impl->dsn~verify-own-version~1]
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
     * Accessor for XML document, called internally and in tests.
     *
     * @param document XML DOM document to retrieve latest version from
     * @return latest version.
     * @throws XmlContentException
     */
    static String getLatestVersion(final Document document) throws XmlContentException {
        final Node node = runXPath(document, LATEST_VERSION_XPATH);
        if (node == null) {
            throw new XmlContentException("Couldn't find node " + LATEST_VERSION_XPATH);
        }
        return node.getTextContent();
    }

    static final String BASE_URL = "https://repo1.maven.org/maven2/";
    static final String METADATA_FILE = "/maven-metadata.xml";
    private static final String PROJECT_KEEPER_PREFIX = "com/exasol/project-keeper-";

    // sonar requests to get this URI from a customizable parameter which is inappropriate in the current situation
    @SuppressWarnings("java:S1075")
    private static final String LATEST_VERSION_XPATH = "/metadata/versioning/latest";

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
     * Get latest project-keeper version.
     * 
     * @throws ParserConfigurationException in configuring parser failed (implementation error)
     * @throws SAXException                 in case XML is invalid
     * @throws IOException                  if URL cannot be connected
     * @throws XmlContentException          in case Maven metadata XML document does not contains expected XML elements
     *                                      with latest version
     * @return latest version of project-keeper in the flavor addressed by the URL of this repository.
     */
    public String getLatestVersion()
            throws ParserConfigurationException, SAXException, IOException, XmlContentException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        final DocumentBuilder db = factory.newDocumentBuilder();
        try (InputStream stream = new URL(this.url).openStream()) {
            return getLatestVersion(db.parse(stream));
        }
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
