package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_JAR_ARTIFACT;

import java.util.Collection;

import org.w3c.dom.Node;

/**
 * Template for maven-assembly-plugin.
 */
public class AssemblyPluginPomTemplate extends AbstractPluginPomTemplate {

    public AssemblyPluginPomTemplate() {
        super("maven_templates/maven-assembly-plugin.xml");
    }

    @Override
    public String getModule() {
        return MODULE_JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/descriptors/descriptor");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
        verifyOrFixHasElement(plugin, runMode, "configuration/finalName");
    }
}
