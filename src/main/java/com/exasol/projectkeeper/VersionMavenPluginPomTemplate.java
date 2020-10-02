package com.exasol.projectkeeper;

import org.w3c.dom.Node;

public class VersionMavenPluginPomTemplate extends AbstractPluginPomTemplate {

    public static final String VERSIONS_MAVEN_PLUGIN = "versions-maven-plugin";

    public VersionMavenPluginPomTemplate() {
        super(VERSIONS_MAVEN_PLUGIN, "org.codehaus.mojo", "maven_templates/versions-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode)
            throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "configuration/rulesUri");
    }

    @Override
    public String getModule() {
        return AbstractProjectKeeperMojo.MODULE_DEFAULT;
    }
}
