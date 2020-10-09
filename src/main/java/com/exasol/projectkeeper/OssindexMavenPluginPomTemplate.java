package com.exasol.projectkeeper;

import org.w3c.dom.Node;

/**
 * Template for ossindex-maven-plugin.
 */
public class OssindexMavenPluginPomTemplate extends AbstractPluginPomTemplate {
    private static final String PLUGIN_NAME = "ossindex-maven-plugin";

    /**
     * Create a new instance of {@link OssindexMavenPluginPomTemplate}.
     */
    public OssindexMavenPluginPomTemplate() {
        super(PLUGIN_NAME, "org.sonatype.ossindex.maven", "maven_templates/ossindex-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode)
            throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions");
    }

    @Override
    public String getModule() {
        return AbstractProjectKeeperMojo.MODULE_DEFAULT;
    }
}
