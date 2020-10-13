package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for maven-assembly-plugin.
 */
public class AssemblyPluginPomTemplate extends AbstractPluginPomTemplate {

    public AssemblyPluginPomTemplate() {
        super("maven_templates/maven-assembly-plugin.xml");
    }

    @Override
    public Module getModule() {
        return Module.JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/descriptors/descriptor");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
        verifyOrFixHasElement(plugin, runMode, "configuration/finalName");
    }
}
