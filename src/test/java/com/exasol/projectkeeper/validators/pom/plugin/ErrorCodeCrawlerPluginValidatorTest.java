package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.validators.pom.PomRenderer.renderPom;
import static com.exasol.projectkeeper.validators.pom.PomTesting.invalidatePom;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.ProjectKeeperModule;

class ErrorCodeCrawlerPluginValidatorTest extends AbstractMavenPluginPomValidatorTestBase {

    ErrorCodeCrawlerPluginValidatorTest() {
        super(new ErrorCodeCrawlerPluginValidator());
    }

    @ValueSource(strings = { "executions", "executions/execution", })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(false));
    }

    @Test
    void testMissingLombokConfig() throws ParserConfigurationException, IOException, SAXException {
        final Document validPom = getFixedPom();
        final Node plugin = runXPath(validPom, "/project/build/plugins/plugin");
        assertThat(isPluginConfigurationValid(plugin, List.of(ProjectKeeperModule.LOMBOK)), equalTo(false));
    }

    @Test
    void testLombokFix() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        final Document validPom = getFixedPom();
        runAllFixesGeneratedByValidation(validPom, List.of(ProjectKeeperModule.LOMBOK));
        final Node plugin = runXPath(validPom, "/project/build/plugins/plugin");
        assertAll(//
                () -> assertThat(renderPom(validPom), containsString(
                        "<configuration><sourcePaths><sourcePath>target/delombok/main</sourcePath><sourcePath>target/delombok/test</sourcePath></sourcePaths></configuration>")),
                () -> assertThat(isPluginConfigurationValid(plugin, List.of(ProjectKeeperModule.LOMBOK)),
                        equalTo(true)));
    }

    @Test
    void testWithoutLombok() throws ParserConfigurationException, IOException, SAXException {
        final Document validPom = getFixedPom();
        final Node plugin = runXPath(validPom, "/project/build/plugins/plugin");
        assertThat(isPluginConfigurationValid(plugin, Collections.emptyList()), equalTo(true));
    }
}