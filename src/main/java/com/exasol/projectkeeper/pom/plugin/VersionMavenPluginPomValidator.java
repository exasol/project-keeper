package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.PomValidationFinding;

/**
 * Validator for the versions-maven-plugin's configuration.
 */
public class VersionMavenPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link VersionMavenPluginPomValidator}.
     */
    public VersionMavenPluginPomValidator() {
        super("maven_templates/versions-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/rulesUri", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
