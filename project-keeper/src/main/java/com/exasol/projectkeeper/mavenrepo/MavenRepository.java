package com.exasol.projectkeeper.mavenrepo;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class allows getting the latest version of project-keeper from Maven Central.
 */
//[impl->dsn~verify-own-version~1]
public class MavenRepository {

    /**
     * @return Maven repository using URL for cli artifacts of project-keeper.
     */
    public static MavenRepository cli() {
        return new MavenRepository(url(PROJECT_KEEPER_PREFIX + "cli"));
    }

    /**
     * @return Maven repository using URL for project-keeper maven-plugin.
     */
    public static MavenRepository mavenPlugin() {
        return new MavenRepository(url(PROJECT_KEEPER_PREFIX + "maven-plugin"));
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

    public static String url(final String infix) {
        return BASE_URL + infix + METADATA_FILE;
    }

    static final String BASE_URL = "https://repo1.maven.org/maven2/";
    static final String PROJECT_KEEPER_PREFIX = "com/exasol/project-keeper-";
    static final String METADATA_FILE = "/maven-metadata.xml";
    static String LATEST_VERSION_XPATH = "/metadata/versioning/latest";

    private final String url;

    /**
     * Productive code is expected to use the static methods {@link MavenRepository#cli} and
     * {@link MavenRepository#mavenPlugin}.
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
     * @return latest version of project-keeper in the flavor addressed by the URL of this repository.
     * @throws ParserConfigurationException in configuring parser failed (implementation error)
     * @throws SAXException                 in case XML is invalid
     * @throws IOException                  if URL cannot be connected
     * @throws XmlContentException          in case Maven metadata XML document does not contains expected XML elements
     *                                      with latest version
     */
    public String getLatestVersion()
            throws ParserConfigurationException, SAXException, IOException, XmlContentException {
        final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
         * @param message the detail message.
         */
        public XmlContentException(final String message) {
            super(message);
        }
    }

    /**
     * @return URL of this Maven repository
     */
    public String getUrl() {
        return this.url;
    }
}
