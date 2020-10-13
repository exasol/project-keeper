package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class AssemblyPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    AssemblyPluginPomTemplateTest() {
        super(new AssemblyPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution", "configuration", "configuration/finalName" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new AssemblyPluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "executions", "executions/execution", "configuration", "configuration/finalName" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final AssemblyPluginPomTemplate template = new AssemblyPluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, Collections.emptyList());
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }
}