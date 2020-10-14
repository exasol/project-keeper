package com.exasol.projectkeeper.pom.plugin;

import static com.exasol.projectkeeper.pom.PomTesting.invalidatePom;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class EnforcerMavenPluginPomValidatorTest extends AbstractMavenPluginPomValidatorTest {

    EnforcerMavenPluginPomValidatorTest() {
        super(new EnforcerMavenPluginPomValidator());
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(false));
    }

    @ValueSource(strings = { "executions", "executions/execution", "executions/execution/id" })
    @ParameterizedTest
    void testFixExecutions(final String removeXpath) throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        runFixesFromValidatePluginConfiguration(plugin, Collections.emptyList());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true));
    }
}