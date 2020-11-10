package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.ProjectKeeperModule.INTEGRATION_TESTS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.invalidatePom;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.ProjectKeeperModule;

class JacocoPluginPomValidatorTest extends AbstractMavenPluginPomValidatorTest {

    JacocoPluginPomValidatorTest() {
        super(new JacocoPluginPomValidator());
    }

    @ValueSource(strings = { "executions", "executions/execution[id/text() = 'prepare-agent']",
            "executions/execution[id/text() = 'prepare-agent']/goals", "executions/execution[id/text() = 'report']" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(false));
    }

    @ValueSource(strings = { "executions", "executions/execution[id/text() = 'prepare-agent']",
            "executions/execution[id/text() = 'prepare-agent']/goals" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath) throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        runFixesFromValidatePluginConfiguration(plugin, Collections.emptyList());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true));
    }

    @ValueSource(strings = { "executions/execution[id/text() = 'prepare-agent-integration']",
            "executions/execution[id/text() = 'report-integration']" })
    @ParameterizedTest
    void testIntegrationTestConfigurationIsNotRequiredForNoIntegration(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertAll(//
                () -> assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true)), //
                () -> assertThat(isPluginConfigurationValid(plugin, List.of(INTEGRATION_TESTS)), equalTo(false)) //
        );
    }

    @ValueSource(strings = { "executions/execution[id/text() = 'prepare-agent-integration']",
            "executions/execution[id/text() = 'report-integration']" })
    @ParameterizedTest
    void testFixIntegrationTestsConfiguration(final String removeXpath)
            throws IOException, SAXException, ParserConfigurationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        final List<ProjectKeeperModule> enabledModules = List.of(INTEGRATION_TESTS);
        runFixesFromValidatePluginConfiguration(plugin, enabledModules);
        assertThat(isPluginConfigurationValid(plugin, enabledModules), equalTo(true));
    }
}