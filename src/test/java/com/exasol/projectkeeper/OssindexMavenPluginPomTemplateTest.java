package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.PomTemplate.RunMode.VERIFY;
import static com.exasol.projectkeeper.PomTesting.invalidatePom;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class OssindexMavenPluginPomTemplateTest extends AbstractMavenPluginPomTemplateTest {
    private static final String POM_WITH_OSSINDEX_PLUGIN = "pomWithOssindexPlugin.xml";

    OssindexMavenPluginPomTemplateTest() {
        super(POM_WITH_OSSINDEX_PLUGIN, new OssindexMavenPluginPomTemplate());
    }

    @ValueSource(strings = { "executions", "executions/execution", })
    @ParameterizedTest
    void testMissingExecutions(final String removeXpath)
            throws ParserConfigurationException, SAXException, IOException {
        final Node plugin = invalidatePom(removeXpath, POM_WITH_OSSINDEX_PLUGIN);
        assertThrows(PomTemplateValidationException.class,
                () -> new OssindexMavenPluginPomTemplate().validatePluginConfiguration(plugin, VERIFY));
    }
}