package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.*;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

/**
 * Validator for error-code-crawler-maven-plugin.
 */
public class ErrorCodeCrawlerPluginTemplateGenerator implements PluginTemplateGenerator {
    private static final String TEMPLATE = "maven_templates/error-code-crawler-maven-plugin.xml";

    /** Create a new instance */
    public ErrorCodeCrawlerPluginTemplateGenerator() {
        // Empty constructor required by javadoc
    }

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        final Node pluginTemplate = new PluginTemplateReader().readPluginTemplate(TEMPLATE);
        if (enabledModules.contains(ProjectKeeperModule.LOMBOK)) {
            addLombokSourcePathConfig(pluginTemplate);
        }
        return Optional.of(pluginTemplate);
    }

    private void addLombokSourcePathConfig(final Node pluginTemplate) {
        final Document document = pluginTemplate.getOwnerDocument();
        final Element sourcePaths = document.createElement("sourcePaths");
        sourcePaths.appendChild(createSourcePathElement(document, "target/delombok/main"));
        final Element configuration = document.createElement("configuration");
        configuration.appendChild(sourcePaths);
        pluginTemplate.appendChild(configuration);
    }

    private Element createSourcePathElement(final Document document, final String sourcePath) {
        final Element sourcePathElement = document.createElement("sourcePath");
        sourcePathElement.setTextContent(sourcePath);
        return sourcePathElement;
    }
}
