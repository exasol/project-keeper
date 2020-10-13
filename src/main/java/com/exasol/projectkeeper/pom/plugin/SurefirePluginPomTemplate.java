package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

public class SurefirePluginPomTemplate extends AbstractPluginPomTemplate {
    public SurefirePluginPomTemplate() {
        super("maven_templates/maven-surefire-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/argLine");
    }

    @Override
    public Module getModule() {
        return Module.DEFAULT;
    }
}
