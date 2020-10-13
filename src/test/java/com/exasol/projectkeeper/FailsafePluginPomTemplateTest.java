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

class FailsafePluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    FailsafePluginPomTemplateTest() {
        super(new FailsafePluginPomTemplate());
    }

    @ValueSource(strings = { "configuration", "configuration/argLine", "executions", "executions/execution" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new FailsafePluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "configuration", "configuration/argLine", "configuration/argLine/text()", "executions",
            "executions/execution" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final FailsafePluginPomTemplate template = new FailsafePluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, Collections.emptyList());
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }
}