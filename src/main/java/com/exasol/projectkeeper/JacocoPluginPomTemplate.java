package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_DEFAULT;
import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_INTEGRATION_TESTS;

import java.util.Collection;

import org.w3c.dom.Node;

/**
 * Template for jacoco-maven-plugin.
 */
public class JacocoPluginPomTemplate extends AbstractPluginPomTemplate {

    public JacocoPluginPomTemplate() {
        super("maven_templates/jacoco-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions/execution[id/text() = 'prepare-agent']");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions/execution[id/text() = 'report']");
        if (enabledModules.contains(MODULE_INTEGRATION_TESTS)) {
            verifyOrFixPluginPropertyHasExactValue(plugin, runMode,
                    "executions/execution[id/text() = 'prepare-agent-integration']");
            verifyOrFixPluginPropertyHasExactValue(plugin, runMode,
                    "executions/execution[id/text() = 'report-integration']");
        }
    }

    @Override
    public String getModule() {
        return MODULE_DEFAULT;
    }
}
