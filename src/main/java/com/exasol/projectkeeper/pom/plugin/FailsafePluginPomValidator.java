package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.PomValidationFinding;

/**
 * Validator for the maven-failsafe-plugin's configuration.
 */
public class FailsafePluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link FailsafePluginPomValidator}.
     */
    public FailsafePluginPomValidator() {
        super("maven_templates/maven-failsafe-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/argLine", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.INTEGRATION_TESTS;
    }
}
