package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for versions-maven-plugin.
 */
public class VersionMavenPluginPomTemplate extends AbstractPluginPomTemplate {

    public VersionMavenPluginPomTemplate() {
        super("maven_templates/versions-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/rulesUri");
    }

    @Override
    public Module getModule() {
        return Module.DEFAULT;
    }
}
