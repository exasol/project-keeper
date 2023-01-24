package com.exasol.projectkeeper.mavenrepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.mavenrepo.MavenRepository.XmlContentException;

//[utest->dsn~verify-own-version~1]
class MavenRepositoryTest {

    @Test
    void url() throws MalformedURLException {
        final String url = MavenRepository.cli().getUrl();
        assertThat(url, startsWith(MavenRepository.BASE_URL));
        assertThat(url, endsWith(MavenRepository.METADATA_FILE));
        assertThat(url, containsString("-cli"));
    }

    @Test
    void testFailureXmlWithoutVersionInformation() {
        final Document xml = xmlDocument("<metadata><versioning><other>1.2.3</other></versioning></metadata>");
        assertThrows(XmlContentException.class, () -> MavenRepository.getLatestVersion(xml));
    }

    @Test
    void testSuccessLocalResource() throws Exception {
        final String url = MavenRepositoryTest.class //
                .getResource("/simulated-maven-central-version-response.xml") //
                .toExternalForm();
        final MavenRepository testee = new MavenRepository(url);
        assertThat(testee.getLatestVersion(), equalTo("2.9.1"));
    }

    @Tag("integration")
    @Test
    // [itest->dsn~verify-own-version~1]
    void integrationTest() throws Exception {
        assertThat(MavenRepository.cli().getLatestVersion(), matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
        assertThat(MavenRepository.mavenPlugin().getLatestVersion(), matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
    }

    private Document xmlDocument(final String content) {
        try (InputStream stream = new ByteArrayInputStream(content.getBytes())) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
