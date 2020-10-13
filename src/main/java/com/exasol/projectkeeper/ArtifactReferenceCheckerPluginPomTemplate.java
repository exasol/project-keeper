package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_JAR_ARTIFACT;

import java.util.Collection;

import org.w3c.dom.Node;

/**
 * Template for artifact-reference-checker-maven-plugin.
 */
public class ArtifactReferenceCheckerPluginPomTemplate extends AbstractPluginPomTemplate {
    public ArtifactReferenceCheckerPluginPomTemplate() {
        super("maven_templates/artifact-reference-checker-maven-plugin.xml");
    }

    @Override
    public String getModule() {
        return MODULE_JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode,
            final Collection<String> enabledModules) throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }
}
