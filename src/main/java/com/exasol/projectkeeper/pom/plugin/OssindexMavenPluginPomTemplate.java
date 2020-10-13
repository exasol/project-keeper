package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for ossindex-maven-plugin.
 */
public class OssindexMavenPluginPomTemplate extends AbstractPluginPomTemplate {

    /**
     * Create a new instance of {@link OssindexMavenPluginPomTemplate}.
     */
    public OssindexMavenPluginPomTemplate() {
        super("maven_templates/ossindex-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }

    @Override
    public Module getModule() {
        return Module.DEFAULT;
    }
}
