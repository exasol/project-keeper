package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;

/**
 * Interfaces for classes that generate pom file configuration for maven plugins.
 */
public interface PluginTemplateGenerator {
    /**
     * Generate a configuration.
     * 
     * @param enabledModules list of enabled modules
     * @return XML object
     */
    Optional<Node> generateTemplate(Collection<ProjectKeeperModule> enabledModules);
}
