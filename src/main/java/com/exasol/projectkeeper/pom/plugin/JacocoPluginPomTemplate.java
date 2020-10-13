package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for jacoco-maven-plugin.
 */
public class JacocoPluginPomTemplate extends AbstractPluginPomTemplate {

    public JacocoPluginPomTemplate() {
        super("maven_templates/jacoco-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions/execution[id/text() = 'prepare-agent']");
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions/execution[id/text() = 'report']");
        if (enabledModules.contains(Module.INTEGRATION_TESTS)) {
            verifyOrFixPluginPropertyHasExactValue(plugin, runMode,
                    "executions/execution[id/text() = 'prepare-agent-integration']");
            verifyOrFixPluginPropertyHasExactValue(plugin, runMode,
                    "executions/execution[id/text() = 'report-integration']");
        }
    }

    @Override
    public Module getModule() {
        return Module.DEFAULT;
    }
}
