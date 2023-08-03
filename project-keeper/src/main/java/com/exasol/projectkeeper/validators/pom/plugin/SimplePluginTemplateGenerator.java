package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

/**
 * Validator for maven-deploy-plugin.
 */
public class SimplePluginTemplateGenerator implements PluginTemplateGenerator {
    private final String templateResource;
    private final ProjectKeeperModule module;

    /**
     * Create a new instance.
     * 
     * @param templateResource template resource
     * @param module           module
     */
    public SimplePluginTemplateGenerator(final String templateResource, final ProjectKeeperModule module) {
        this.templateResource = templateResource;
        this.module = module;
    }

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        if (enabledModules.contains(this.module)) {
            return Optional.of(new PluginTemplateReader().readPluginTemplate(this.templateResource));
        } else {
            return Optional.empty();
        }
    }
}
