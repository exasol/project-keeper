package com.exasol.projectkeeper.mavenrepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.mavenrepo.MavenRepository.XmlContentException;

//[utest->dsn~verify-own-version~2]
class MavenRepositoryTest {

    @Test
    void url() {
        final String url = MavenRepository.projectKeeperCli().getUrl();
        assertThat(url, equalTo("https://repo1.maven.org/maven2/com/exasol/project-keeper-cli/maven-metadata.xml"));
    }

    @Test
    void testGetLatestStableVersionUsesMavenVersionOrdering() throws Exception {
        final Document xml = xmlDocument("""
                <metadata><versioning><versions>
                    <version>1.9.0</version><version>1.10.0</version><version>2.0.0-beta</version>
                </versions></versioning></metadata>
                """);
        assertThat(MavenRepository.getLatestStableVersion(xml), equalTo("1.10.0"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1.1-alpha", "1.1-A1", "1.1-BeTa", "1.1-M1", "1.1-RC1", "1.1-cR2",
            "1.1-SNAPSHOT", "1.1-preview1", "1.1-ea", "1.1-1" })
    void testGetLatestStableVersionExcludesPreReleaseQualifiers(final String preReleaseVersion) throws Exception {
        final Document xml = xmlDocument("<metadata><versioning><versions><version>1.0.0</version><version>"
                + preReleaseVersion + "</version></versions></versioning></metadata>");
        assertThat(MavenRepository.getLatestStableVersion(xml), equalTo("1.0.0"));
    }

    @Test
    void testGetLatestStableVersionFailsWithoutStableVersion() {
        final Document xml = xmlDocument("<metadata><versioning><versions><version>1.0-beta</version>"
                + "<version>1.0-RC1</version><version>1.0-SNAPSHOT</version></versions></versioning></metadata>");
        final XmlContentException exception = assertThrows(XmlContentException.class,
                () -> MavenRepository.getLatestStableVersion(xml));
        assertThat(exception.getMessage(), equalTo("Couldn't find a stable version in node /metadata/versioning/versions"));
    }

    @Test
    void testGetLatestStableVersionFailsForVersionWithUnparseableComponent() {
        final Document xml = xmlDocument("""
                <metadata><versioning><versions>
                    <version>2147483648.0.0</version>
                </versions></versioning></metadata>
                """);
        assertThrows(XmlContentException.class, () -> MavenRepository.getLatestStableVersion(xml));
    }

    @Test
    void testGetLatestStableVersionWrapsCheckedExceptions() {
        final MavenRepository repository = new MavenRepository("file:///non-existent-maven-metadata.xml");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                repository::getLatestStableVersion);
        assertThat(exception.getMessage(), equalTo("Couldn't get latest stable version from file:///non-existent-maven-metadata.xml"));
    }

    @Tag("integration")
    @Test
    // [itest->dsn~verify-own-version~2]
    void integrationTest() {
        assertAll(() -> assertThat(MavenRepository.projectKeeperCli().getLatestStableVersion(), matchesRegex("\\d+\\.\\d+\\.\\d+")),
                () -> assertThat(MavenRepository.projectKeeperMavenPlugin().getLatestStableVersion(),
                        matchesRegex("\\d+\\.\\d+\\.\\d+")));
    }

    private Document xmlDocument(final String content) {
        try (InputStream stream = new ByteArrayInputStream(content.getBytes())) {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
