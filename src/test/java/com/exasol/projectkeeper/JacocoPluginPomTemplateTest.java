package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_INTEGRATION_TESTS;
import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class JacocoPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    JacocoPluginPomTemplateTest() {
        super(new JacocoPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution[id/text() = 'prepare-agent']",
            "executions/execution[id/text() = 'prepare-agent']/goals", "executions/execution[id/text() = 'report']" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new JacocoPluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "executions", "executions/execution[id/text() = 'prepare-agent']",
            "executions/execution[id/text() = 'prepare-agent']/goals" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final JacocoPluginPomTemplate template = new JacocoPluginPomTemplate();
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, Collections.emptyList());
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { "executions/execution[id/text() = 'prepare-agent-integration']",
            "executions/execution[id/text() = 'report-integration']" })
    @ParameterizedTest
    void testIntegrationTestConfigurationIsNotRequiredForNoIntegration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertAll(//
                () -> assertDoesNotThrow(() -> new JacocoPluginPomTemplate().validatePluginConfiguration(plugin, VERIFY,
                        Collections.emptyList())), //
                () -> assertThrows(PomTemplateValidationException.class, () -> new JacocoPluginPomTemplate()
                        .validatePluginConfiguration(plugin, VERIFY, List.of(MODULE_INTEGRATION_TESTS)))//
        );
    }

    @ValueSource(strings = { "executions/execution[id/text() = 'prepare-agent-integration']",
            "executions/execution[id/text() = 'report-integration']" })
    @ParameterizedTest
    void testFixIntegrationTestsConfiguration(final String removeXpath)
            throws PomTemplateValidationException, IOException, SAXException, ParserConfigurationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final JacocoPluginPomTemplate template = new JacocoPluginPomTemplate();
        final List<String> enabledModules = List.of(MODULE_INTEGRATION_TESTS);
        template.validatePluginConfiguration(plugin, PomTemplate.RunMode.FIX, enabledModules);
        assertDoesNotThrow(() -> template.validatePluginConfiguration(plugin, VERIFY, enabledModules));
    }
}