package com.exasol.projectkeeper.validators.pom.plugin;

import static com.exasol.projectkeeper.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;

/**
 * Validator for the maven-failsafe-plugin's configuration.
 */
public class FailsafePluginTemplateGenerator implements PluginTemplateGenerator {
    private static final String TEST_COVERAGE_CONFIGURATION = "configuration/systemPropertyVariables/test.coverage";
    private static final String TEMPLATE = "maven_templates/maven-failsafe-plugin.xml";

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        if (enabledModules.contains(ProjectKeeperModule.INTEGRATION_TESTS)) {
            final Node pluginTemplate = new PluginTemplateReader().readPluginTemplate(TEMPLATE);
            if (!enabledModules.contains(ProjectKeeperModule.UDF_COVERAGE)) {
                removeCoverageConfig(pluginTemplate);
            }
            return Optional.of(pluginTemplate);
        } else {
            return Optional.empty();
        }
    }

    private void removeCoverageConfig(final Node pluginTemplate) {
        final Node coverageConfig = runXPath(pluginTemplate, TEST_COVERAGE_CONFIGURATION);
        coverageConfig.getParentNode().removeChild(coverageConfig);
    }
}
