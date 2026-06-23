package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;

/**
 * Generates a plugin template if any of the configured modules is enabled.
 */
public class AnyOfModulesPluginTemplateGenerator implements PluginTemplateGenerator {
    private final String templateResource;
    private final Set<ProjectKeeperModule> modules;

    /**
     * Create a new instance.
     *
     * @param templateResource template resource
     * @param modules          modules that trigger the template
     */
    public AnyOfModulesPluginTemplateGenerator(final String templateResource, final Set<ProjectKeeperModule> modules) {
        this.templateResource = templateResource;
        this.modules = modules;
    }

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        if (enabledModules.stream().anyMatch(this.modules::contains)) {
            return Optional.of(new PluginTemplateReader().readPluginTemplate(this.templateResource));
        } else {
            return Optional.empty();
        }
    }
}
