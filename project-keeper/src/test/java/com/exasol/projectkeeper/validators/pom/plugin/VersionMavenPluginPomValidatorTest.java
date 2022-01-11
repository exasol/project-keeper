package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.validators.pom.PomTesting.invalidatePom;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class VersionMavenPluginPomValidatorTest extends AbstractMavenPluginPomValidatorTestBase {

    VersionMavenPluginPomValidatorTest() {
        super(new VersionMavenPluginPomValidator());
    }

    @CsvSource({ //
            "configuration", //
            "configuration/rulesUri" //
    })
    @ParameterizedTest
    void testFixes(final String removeXpath) throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        runFixesFromValidatePluginConfiguration(plugin, Collections.emptyList());
        assertThat(plugin, hasXPath("configuration/rulesUri"));
    }

    @CsvSource({ //
            "configuration, E-PK-13: The versions-maven-plugin's configuration does not contain the required property 'configuration/rulesUri'.", //
            "configuration/rulesUri, E-PK-13: The versions-maven-plugin's configuration does not contain the required property 'configuration/rulesUri'.", //
            "configuration/rulesUri/text(), E-PK-14: The versions-maven-plugin's configuration-property 'configuration/rulesUri' has an illegal value."//
    })
    @ParameterizedTest
    void testVerifyConfiguration(final String removeXpath, final String expectedError)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final List<String> messages = new ArrayList<>(1);
        new VersionMavenPluginPomValidator().validatePluginConfiguration(plugin, Collections.emptyList(),
                finding -> messages.add(finding.getMessage()));
        assertThat(messages, contains(expectedError));
    }

    @CsvSource({ //
            "configuration", //
            "configuration/rulesUri", //
            "configuration/rulesUri/text()"//
    })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath) throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        runFixesFromValidatePluginConfiguration(plugin, Collections.emptyList());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true));
    }
}