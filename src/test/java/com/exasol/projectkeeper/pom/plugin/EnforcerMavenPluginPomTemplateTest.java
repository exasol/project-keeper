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

class EnforcerMavenPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    EnforcerMavenPluginPomTemplateTest() {
        super(new EnforcerMavenPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new EnforcerMavenPluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testFixExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final EnforcerMavenPluginPomTemplate template = new EnforcerMavenPluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, Collections.emptyList());
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }
}