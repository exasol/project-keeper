package com.exasol.projectkeeper.mavenrepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;

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
    void url() {
        final String url = MavenRepository.projectKeeperCli().getUrl();
        assertThat(url, equalTo("https://repo1.maven.org/maven2/com/exasol/project-keeper-cli/maven-metadata.xml"));
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
        assertThat(MavenRepository.projectKeeperCli().getLatestVersion(), matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
        assertThat(MavenRepository.projectKeeperMavenPlugin().getLatestVersion(),
                matchesRegex("[0-9]+\\.[0-9]+\\.[0-9]+"));
    }

    private Document xmlDocument(final String content) {
        try (InputStream stream = new ByteArrayInputStream(content.getBytes())) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
