package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for the maven-enforcer-plugin.
 */
public class EnforcerMavenPluginPomTemplate extends AbstractPluginPomTemplate {

    /**
     * Create a new instance of {@link EnforcerMavenPluginPomTemplate}.
     */
    public EnforcerMavenPluginPomTemplate() {
        super("maven_templates/maven-enforcer-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions[execution/id/text() = 'enforce-maven']");
    }

    @Override
    public Module getModule() {
        return Module.DEFAULT;
    }
}
