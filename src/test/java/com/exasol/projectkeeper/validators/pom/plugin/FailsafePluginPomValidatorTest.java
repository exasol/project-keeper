package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.validators.pom.PomTesting.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlunit.matchers.EvaluateXPathMatcher;

import com.exasol.projectkeeper.ProjectKeeperModule;

class FailsafePluginPomValidatorTest extends AbstractMavenPluginPomValidatorTestBase {

    FailsafePluginPomValidatorTest() {
        super(new FailsafePluginPomValidator());
    }

    @ValueSource(strings = { "configuration", "configuration/argLine", "executions", "executions/execution" })
    @ParameterizedTest
    void testInvalidThrowsException(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(false));
    }

    @ValueSource(strings = { "configuration", "configuration/argLine", "configuration/argLine/text()", "executions",
            "executions/execution" })
    @ParameterizedTest
    void testFixConfiguration(final String removeXpath) throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        runFixesFromValidatePluginConfiguration(plugin, Collections.emptyList());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true));
    }

    @Test
    void testFixWithoutUdfIntegration() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, Collections.emptyList());
        assertThat(pom,
                not(hasXPath("/project/build/plugins/plugin/configuration/systemPropertyVariables/test.coverage")));
    }

    @Test
    void testFixWithUdfIntegration() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, List.of(ProjectKeeperModule.UDF_COVERAGE));
        assertThat(pom, EvaluateXPathMatcher.hasXPath(
                "/project/build/plugins/plugin/configuration/systemPropertyVariables/test.coverage", equalTo("true")));
    }
}