package com.exasol.projectkeeper;

import org.w3c.dom.Node;

/**
 * Template for the maven-enforcer-plugin.
 */
public class EnforcerMavenPluginPomTemplate extends AbstractPluginPomTemplate {
    private static final String PLUGIN_NAME = "maven-enforcer-plugin";

    /**
     * Create a new instance of {@link EnforcerMavenPluginPomTemplate}.
     */
    public EnforcerMavenPluginPomTemplate() {
        super(PLUGIN_NAME, "org.apache.maven.plugins", "maven_templates/maven-enforcer-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final RunMode runMode)
            throws PomTemplateValidationException {
        verifyOrFixPluginPropertyHasExactValue(plugin, runMode, "executions[execution/id/text() = 'enforce-maven']");
    }

    @Override
    public String getModule() {
        return AbstractProjectKeeperMojo.MODULE_DEFAULT;
    }
}
