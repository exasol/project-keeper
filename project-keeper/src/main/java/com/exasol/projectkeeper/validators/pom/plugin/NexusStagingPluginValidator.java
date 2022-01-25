package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for nexus-staging-maven-plugin.
 */
public class NexusStagingPluginValidator extends AbstractPluginPomValidator {
    /**
     * Create a new instance of {@link NexusStagingPluginValidator}.
     */
    public NexusStagingPluginValidator() {
        super("maven_templates/nexus-staging-maven-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.MAVEN_CENTRAL;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/autoReleaseAfterClose", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/serverId", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/nexusUrl", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'default-deploy']",
                findingConsumer);
    }
}
