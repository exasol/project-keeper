package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.FIX;
import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.getParsedPom;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public abstract class AbstractMavenPluginPomTemplateTest {
    private final String pomWithValidPlugin;
    private final PomTemplate template;

    public AbstractMavenPluginPomTemplateTest(final String pomWithValidPlugin, final PomTemplate template) {
        this.pomWithValidPlugin = pomWithValidPlugin;
        this.template = template;
    }

    @Test
    void testMissingPlugin() throws IOException, ParserConfigurationException, SAXException {
        final Document pom = invalidatePom(".", this.pomWithValidPlugin).getOwnerDocument();
        assertThrows(PomTemplateValidationException.class, () -> this.template.run(pom, VERIFY));
    }

    @Test
    void testVerifyValidPlugin() throws IOException, ParserConfigurationException, SAXException {
        final Document pom = getParsedPom(this.pomWithValidPlugin);
        assertDoesNotThrow(() -> this.template.run(pom, VERIFY));
    }

    @Test
    void testFix() throws IOException, ParserConfigurationException, SAXException, PomTemplateValidationException {
        final Document pom = invalidatePom(".", this.pomWithValidPlugin).getOwnerDocument();
        this.template.run(pom, FIX);
        assertDoesNotThrow(() -> this.template.run(pom, VERIFY));
    }
}
