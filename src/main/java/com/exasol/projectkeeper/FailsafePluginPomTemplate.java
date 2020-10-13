package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_INTEGRATION_TESTS;

import java.util.Collection;

import org.w3c.dom.Node;

/**
 * Template for maven-failsafe-plugin.
 */
public class FailsafePluginPomTemplate extends AbstractPluginPomTemplate {

    public FailsafePluginPomTemplate() {
        super("maven_templates/maven-failsafe-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/argLine");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }

    @Override
    public String getModule() {
        return MODULE_INTEGRATION_TESTS;
    }
}
