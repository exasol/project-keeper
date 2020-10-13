package com.exasol.projectkeeper.pom.plugin;

import static com.exasol.projectkeeper.pom.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.pom.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class OssindexMavenPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {

    OssindexMavenPluginPomTemplateTest() {
        super(new OssindexMavenPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution", })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException, PomTemplateValidationException {
        final Node plugin = invalidatePom(removeXpath, getFixedPom());
        assertThrows(PomTemplateValidationException.class, () -> new OssindexMavenPluginPomTemplate()
                .validatePluginConfiguration(plugin, VERIFY, Collections.emptyList()));
    }
}