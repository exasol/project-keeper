package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.ProjectKeeperModule.INTEGRATION_TESTS;
import static com.exasol.projectkeeper.ProjectKeeperModule.UDF_COVERAGE;
import static com.exasol.projectkeeper.validators.pom.PomTesting.*;
import static com.exasol.xpath.XPathErrorHanlingWrapper.runXPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.hamcrest.xml.HasXPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
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

    @Test
    void testFixIntegrationTestsConfiguration() throws IOException, SAXException, ParserConfigurationException {
        final Node plugin = runXPath(getFixedPom(), "/project/build/plugins/plugin");
        final List<ProjectKeeperModule> enabledModules = List.of(INTEGRATION_TESTS);
        runFixesFromValidatePluginConfiguration(plugin, enabledModules);
        assertThat(isPluginConfigurationValid(plugin, enabledModules), equalTo(true));
    }

    @Test
    void testNonIntegrationFix() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, List.of());
        final Node plugin = runXPath(pom, "/project/build/plugins/plugin");
        assertAll(//
                () -> assertThat(plugin, HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent']")),
                () -> assertThat(plugin,
                        not(HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent-integration']"))),
                () -> assertThat(plugin, not(HasXPath.hasXPath("executions/execution[id/text() = 'merge-it-results']")))//
        );
    }

    @Test
    void testIntegrationFix() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, List.of(INTEGRATION_TESTS));
        final Node plugin = runXPath(pom, "/project/build/plugins/plugin");
        assertAll(//
                () -> assertThat(plugin, HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent']")),
                () -> assertThat(plugin,
                        HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent-integration']")),
                () -> assertThat(plugin, not(HasXPath.hasXPath("executions/execution[id/text() = 'merge-it-results']")))//
        );
    }

    @Test
    void testIntegrationAndCoverageFix() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, List.of(INTEGRATION_TESTS, UDF_COVERAGE));
        final Node plugin = runXPath(pom, "/project/build/plugins/plugin");
        assertAll(//
                () -> assertThat(plugin, HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent']")),
                () -> assertThat(plugin,
                        HasXPath.hasXPath("executions/execution[id/text() = 'prepare-agent-integration']")),
                () -> assertThat(plugin, HasXPath.hasXPath("executions/execution[id/text() = 'merge-it-results']"))//
        );
    }
}