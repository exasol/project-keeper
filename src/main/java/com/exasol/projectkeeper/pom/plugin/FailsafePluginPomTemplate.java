package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for maven-failsafe-plugin.
 */
public class FailsafePluginPomTemplate extends AbstractPluginPomTemplate {

    public FailsafePluginPomTemplate() {
        super("maven_templates/maven-failsafe-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/argLine");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }

    @Override
    public Module getModule() {
        return Module.INTEGRATION_TESTS;
    }
}
