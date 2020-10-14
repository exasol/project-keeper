package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.PomValidationFinding;

/**
 * Template for ossindex-maven-plugin.
 */
public class OssindexMavenPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link OssindexMavenPluginPomValidator}.
     */
    public OssindexMavenPluginPomValidator() {
        super("maven_templates/ossindex-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
