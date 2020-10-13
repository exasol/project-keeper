package com.exasol.projectkeeper.pom.plugin;

import static com.exasol.projectkeeper.pom.PomTemplate.RunMode.FIX;
import static com.exasol.projectkeeper.pom.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.pom.PomTesting.readXmlFromResources;
import static com.exasol.xpath.XPathErrorHanlingWrapper.runXpath;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.exasol.projectkeeper.pom.PomTemplate;

public abstract class AbstractMavenPluginPomTemplateTest {
    public static final String POM_WITH_NO_PLUGINS = "pomWithNoPlugins.xml";
    private final PomTemplate template;

    public AbstractMavenPluginPomTemplateTest(final PomTemplate template) {
        this.template = template;
    }

    @Test
    void testMissingPlugin() throws IOException, ParserConfigurationException, SAXException {
        final Document emptyPom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        assertThrows(PomTemplateValidationException.class,
                () -> this.template.run(emptyPom, VERIFY, Collections.emptyList()));
    }

    @Test
    void testFix() throws IOException, ParserConfigurationException, SAXException, PomTemplateValidationException {
        final Document emptyPom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        this.template.run(emptyPom, FIX, Collections.emptyList());
        assertDoesNotThrow(() -> this.template.run(emptyPom, VERIFY, Collections.emptyList()));
    }

    @ValueSource(strings = { //
            "/project/build", "/project/build/plugins"//
    })
    @ParameterizedTest
    void testFixMissingObjects(final String removeXPath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        final Node elementToRemove = runXpath(pom, removeXPath);
        elementToRemove.getParentNode().removeChild(elementToRemove);
        this.template.run(pom, FIX, Collections.emptyList());
        assertDoesNotThrow(() -> this.template.run(pom, VERIFY, Collections.emptyList()));
    }

    protected Document getFixedPom()
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Document pom = readXmlFromResources(POM_WITH_NO_PLUGINS);
        this.template.run(pom, FIX, Collections.emptyList());
        return pom;
    }
}
