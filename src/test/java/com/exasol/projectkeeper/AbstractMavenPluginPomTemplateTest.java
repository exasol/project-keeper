package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.FIX;
import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.readXmlFromResources;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class AbstractMavenPluginPomTemplateTest {
    private final PomTemplate template;

    public AbstractMavenPluginPomTemplateTest(final PomTemplate template) {
        this.template = template;
    }

    @Test
    void testMissingPlugin() throws IOException, ParserConfigurationException, SAXException {
        final Document emptyPom = readXmlFromResources("pomWithNoPlugins.xml");
        assertThrows(PomTemplateValidationException.class,
                () -> this.template.run(emptyPom, VERIFY, Collections.emptyList()));
    }

    @Test
    void testFix() throws IOException, ParserConfigurationException, SAXException, PomTemplateValidationException {
        final Document emptyPom = readXmlFromResources("pomWithNoPlugins.xml");
        this.template.run(emptyPom, FIX, Collections.emptyList());
        assertDoesNotThrow(() -> this.template.run(emptyPom, VERIFY, Collections.emptyList()));
    }

    protected Document getFixedPom()
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Document pom = readXmlFromResources("pomWithNoPlugins.xml");
        this.template.run(pom, FIX, Collections.emptyList());
        return pom;
    }
}
