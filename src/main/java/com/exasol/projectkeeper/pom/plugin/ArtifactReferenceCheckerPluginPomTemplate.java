package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.Module;

/**
 * Template for artifact-reference-checker-maven-plugin.
 */
public class ArtifactReferenceCheckerPluginPomTemplate extends AbstractPluginPomTemplate {
    public ArtifactReferenceCheckerPluginPomTemplate() {
        super("maven_templates/artifact-reference-checker-maven-plugin.xml");
    }

    @Override
    public Module getModule() {
        return Module.JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<Module> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }
}
