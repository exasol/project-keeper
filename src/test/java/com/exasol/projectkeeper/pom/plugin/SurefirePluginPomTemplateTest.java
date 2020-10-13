package com.exasol.projectkeeper.pom.plugin;

import static com.exasol.projectkeeper.pom.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.pom.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.pom.PomTemplate;

class SurefirePluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    SurefirePluginPomTemplateTest() {
        super(new SurefirePluginPomTemplate());
    }

    @ValueSource(strings = { "configuration", "configuration/argLine" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new SurefirePluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "configuration", "configuration/argLine", "configuration/argLine/text()" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final SurefirePluginPomTemplate template = new SurefirePluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, Collections.emptyList());
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }
}