package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.UDF_COVERAGE;
import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

/**
 * Validator for the jacoco-maven-plugin's configuration.
 */
public class JacocoPluginTemplateGenerator implements PluginTemplateGenerator {
    private static final String TEMPLATE = "maven_templates/jacoco-maven-plugin.xml";

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        final Node pluginTemplate = new PluginTemplateReader().readPluginTemplate(TEMPLATE);
        if (!enabledModules.contains(UDF_COVERAGE)) {
            final Node configToRemove = runXPath(pluginTemplate,
                    "executions/execution[id/text()='prepare-agent-integration']");
            configToRemove.getParentNode().removeChild(configToRemove);
        }
        return Optional.of(pluginTemplate);
    }
}
