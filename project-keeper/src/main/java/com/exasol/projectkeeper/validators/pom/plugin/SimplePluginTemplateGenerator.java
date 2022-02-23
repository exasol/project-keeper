package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;

import lombok.RequiredArgsConstructor;

/**
 * Validator for maven-deploy-plugin.
 */
@RequiredArgsConstructor
public class SimplePluginTemplateGenerator implements PluginTemplateGenerator {
    private final String templateResource;
    private final ProjectKeeperModule module;

    @Override
    public Optional<Node> generateTemplate(final Collection<ProjectKeeperModule> enabledModules) {
        if (enabledModules.contains(this.module)) {
            return Optional.of(new PluginTemplateReader().readPluginTemplate(this.templateResource));
        } else {
            return Optional.empty();
        }
    }
}
