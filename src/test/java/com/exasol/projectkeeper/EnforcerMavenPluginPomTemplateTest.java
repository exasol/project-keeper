package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class EnforcerMavenPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {
    private static final String POM_WITH_ENFORCER_PLUGIN = "pomWithEnforcerPlugin.xml";

    EnforcerMavenPluginPomTemplateTest() {
        super(POM_WITH_ENFORCER_PLUGIN, new EnforcerMavenPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_ENFORCER_PLUGIN);
        assertThrows(PomTemplateValidationException.class,
                () -> new EnforcerMavenPluginPomTemplate().validatePluginConfiguration(plugin, VERIFY));
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testFixExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_ENFORCER_PLUGIN);
        final EnforcerMavenPluginPomTemplate template = new EnforcerMavenPluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX);
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY));
    }
}