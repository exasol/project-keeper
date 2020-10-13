package com.exasol.projectkeeper;

import java.util.Collection;

import org.w3c.dom.Node;

/**
 * Template for versions-maven-plugin.
 */
public class VersionMavenPluginPomTemplate extends AbstractPluginPomTemplate {

    public VersionMavenPluginPomTemplate() {
        super("maven_templates/versions-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/rulesUri");
    }

    @Override
    public String getModule() {
        return AbstractProjectKeeperMojo.MODULE_DEFAULT;
    }
}
