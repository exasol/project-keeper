package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.validators.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.validators.pom.PomTesting.readXmlFromResources;
import static com.exasol.xpath.XPathErrorHanlingWrapper.runXPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.ProjectKeeperModule;

abstract class AbstractMavenPluginPomValidatorTest {
    private final AbstractPluginPomValidator template;

    public AbstractMavenPluginPomValidatorTest(final AbstractPluginPomValidator template) {
        this.template = template;
    }

    @Test
    void testMissingPlugin() throws IOException, ParserConfigurationException, SAXException {
        final Document emptyPom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        assertThat(isValid(emptyPom, Collections.emptyList()), equalTo(false));
    }

    @Test
    void testMissingPluginFix() throws IOException, ParserConfigurationException, SAXException {
        final Document emptyPom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(emptyPom, Collections.emptyList());
        assertAll(//
                () -> assertThat(isValid(emptyPom, Collections.emptyList()), equalTo(true)),
                () -> assertThat(emptyPom, hasXPath("/project/build/plugins/plugin"))//
        );
    }

    @ValueSource(strings = { //
            "/project/build", "/project/build/plugins"//
    })
    @ParameterizedTest
    void testFixMissingObjects(final String removeXPath)
            throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        final Node elementToRemove = runXPath(pom, removeXPath);
        elementToRemove.getParentNode().removeChild(elementToRemove);
        runAllFixesGeneratedByValidation(pom, Collections.emptyList());
        assertThat(pom, hasXPath("/project/build/plugins"));
    }

    protected Document getFixedPom() throws ParserConfigurationException, SAXException, IOException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        runAllFixesGeneratedByValidation(pom, Collections.emptyList());
        return pom;
    }

    private boolean isValid(final Document pom, final Collection<ProjectKeeperModule> enabledModules) {
        final AtomicBoolean success = new AtomicBoolean(true);
        this.template.validate(pom, Collections.emptyList(), finding -> success.set(false));
        return success.get();
    }

    private void runAllFixesGeneratedByValidation(final Document pom,
            final Collection<ProjectKeeperModule> enabledModules) {
        this.template.validate(pom, Collections.emptyList(), finding -> finding.getFix().fixError(mock(Log.class)));
    }

    protected boolean isPluginConfigurationValid(final Node plugin,
            final Collection<ProjectKeeperModule> enabledModules) {
        final AtomicBoolean success = new AtomicBoolean(true);
        this.template.validatePluginConfiguration(plugin, enabledModules, finding -> success.set(false));
        return success.get();
    }

    protected void runFixesFromValidatePluginConfiguration(final Node plugin,
            final Collection<ProjectKeeperModule> enabledModules) {
        this.template.validatePluginConfiguration(plugin, enabledModules,
                finding -> finding.getFix().fixError(mock(Log.class)));
    }
}
