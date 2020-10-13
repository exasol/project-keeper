package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_DEFAULT;

import java.util.Collection;

import org.w3c.dom.Node;

public class SurefirePluginPomTemplate extends AbstractPluginPomTemplate {
    public SurefirePluginPomTemplate() {
        super("maven_templates/maven-surefire-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/argLine");
    }

    @Override
    public String getModule() {
        return MODULE_DEFAULT;
    }
}
