package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.FIX;
import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.getParsedPom;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static com.exasol.projectkeeper.XPathErrorHanlingWrapper.runXpath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class VersionMavenPluginPomTemplateTest {
    private static final String POM_WITH_VERSION_PLUGIN = "pomWithVersionPlugin.xml";

    @Test
    void testMissingPlugin() throws IOException, ParserConfigurationException, SAXException {
        final VersionMavenPluginPomTemplate template = new VersionMavenPluginPomTemplate();
        final Document pom = invalidatePom(".", POM_WITH_VERSION_PLUGIN).getOwnerDocument();
        assertThrows(PomTemplateValidationException.class, () -> template.run(pom, VERIFY));
    }

    @Test
    void testVerifyValidPlugin() throws IOException, ParserConfigurationException, SAXException {
        final VersionMavenPluginPomTemplate template = new VersionMavenPluginPomTemplate();
        final Document pom = getParsedPom(POM_WITH_VERSION_PLUGIN);
        assertDoesNotThrow(() -> template.run(pom, VERIFY));
    }

    @Test
    void testFix() throws IOException, ParserConfigurationException, SAXException, PomTemplateValidationException {
        final VersionMavenPluginPomTemplate template = new VersionMavenPluginPomTemplate();
        final Document pom = invalidatePom(".", POM_WITH_VERSION_PLUGIN).getOwnerDocument();
        template.run(pom, FIX);
        assertDoesNotThrow(() -> template.run(pom, VERIFY));
    }

    @CsvSource({ //
            "configuration", //
            "configuration/rulesUri" //
    })
    @ParameterizedTest
    void testValidateOrFixExits(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_VERSION_PLUGIN);
        final VersionMavenPluginPomTemplate template = new VersionMavenPluginPomTemplate();
        template.verifyOrFixHasElement(plugin, PomTemplate.RunMode.FIX, "configuration/rulesUri");
        assertThat(runXpath(plugin, removeXpath), not(equalTo(null)));
    }

    @CsvSource({ //
            "configuration, pom.xml: The versions-maven-plugin's configuration does not contain the required property configuration/rulesUri.", //
            "configuration/rulesUri, pom.xml: The versions-maven-plugin's configuration does not contain the required property configuration/rulesUri.", //
            "configuration/rulesUri/text(), pom.xml: The versions-maven-plugin's configuration-property configuration/rulesUri has an illegal value."//
    })
    @ParameterizedTest
    void testVerifyConfiguration(final String removeXpath, final String expectedError)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_VERSION_PLUGIN);
        final PomTemplateValidationException exception = assertThrows(PomTemplateValidationException.class,
                () -> new VersionMavenPluginPomTemplate().validatePluginConfiguration(plugin, VERIFY));
        assertThat(exception.getMessage(), equalTo(expectedError));
    }

    @CsvSource({ //
            "configuration", //
            "configuration/rulesUri", //
            "configuration/rulesUri/text()"//
    })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_VERSION_PLUGIN);
        final VersionMavenPluginPomTemplate template = new VersionMavenPluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX);
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY));
    }
}