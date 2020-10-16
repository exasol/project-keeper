package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.PomValidationFinding;

/**
 * Validator for the jacoco-maven-plugin's configuration.
 */
public class JacocoPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link JacocoPluginPomValidator}.
     */
    public JacocoPluginPomValidator() {
        super("maven_templates/jacoco-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'prepare-agent']", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'report']", findingConsumer);
        if (enabledModules.contains(ProjectKeeperModule.INTEGRATION_TESTS)) {
            verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'prepare-agent-integration']",
                    findingConsumer);
            verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'report-integration']",
                    findingConsumer);
        }
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
