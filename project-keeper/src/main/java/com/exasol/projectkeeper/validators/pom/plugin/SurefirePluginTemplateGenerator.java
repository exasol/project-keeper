package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

/**
 * Validator for the maven-surefire-plugin's configuration.
 */
// [impl->dsn~disable-telemetry.unit-tests~1]
public class SurefirePluginTemplateGenerator implements PluginTemplateGenerator {

    private static final String TEMPLATE = "maven_templates/maven-surefire-plugin.xml";
    @SuppressWarnings("s1075") // hard coding URI is ok
    static final String ARG_LINE_XPATH = "/plugin/configuration/argLine";

    /** Create a new instance */
    public SurefirePluginTemplateGenerator() {
        // Empty constructor required by javadoc
    }

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        final Node pluginTemplate = new PluginTemplateReader().readPluginTemplate(TEMPLATE);

        if (enabledModules.contains(ProjectKeeperModule.MOCKITO_AGENT)) {
            final Node argLineNode = runXPath(pluginTemplate, ARG_LINE_XPATH);
            argLineNode.setTextContent("-javaagent:${org.mockito:mockito-core:jar} " + argLineNode.getTextContent());
        }

        return Optional.of(pluginTemplate);
    }
}
