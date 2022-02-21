package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.Optional;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;

public interface PluginTemplateGenerator {

    Optional<Node> generateTemplate(Collection<ProjectKeeperModule> enabledModules);
}
