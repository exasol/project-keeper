package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the jacoco-maven-plugin's configuration.
 */
public class JacocoPluginPomValidator extends AbstractPluginPomValidator {
    private static final String REPORT_INTEGRATION_EXECUTION = "executions/execution[id/text() = 'report-integration']";
    private static final String MERGE_EXECUTION = "executions/execution[id/text() = 'merge-it-results']";
    private static final String REPORT_IT_WITH_UDF_EXECUTION = "executions/execution[id/text() = 'report-integration-with-udf']";
    private static final String PREPARE_IT_AGENT_EXECUTION = "executions/execution[id/text() = 'prepare-agent-integration']";

    /**
     * Create a new instance of {@link JacocoPluginPomValidator}.
     */
    public JacocoPluginPomValidator() {
        super("maven_templates/jacoco-maven-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'prepare-agent']", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'report']", findingConsumer);
        if (enabledModules.contains(ProjectKeeperModule.INTEGRATION_TESTS)) {
            verifyPluginPropertyHasExactValue(plugin, PREPARE_IT_AGENT_EXECUTION, findingConsumer);
            if (enabledModules.contains(ProjectKeeperModule.UDF_COVERAGE)) {
                verifyPluginPropertyHasExactValue(plugin, MERGE_EXECUTION, findingConsumer);
                verifyPluginPropertyHasExactValue(plugin, REPORT_IT_WITH_UDF_EXECUTION, findingConsumer);
                verifyPluginDoesNotHaveProperty(plugin, REPORT_INTEGRATION_EXECUTION, findingConsumer);
            } else {
                verifyPluginPropertyHasExactValue(plugin, REPORT_INTEGRATION_EXECUTION, findingConsumer);
                verifyPluginDoesNotHaveProperty(plugin, MERGE_EXECUTION, findingConsumer);
                verifyPluginDoesNotHaveProperty(plugin, REPORT_IT_WITH_UDF_EXECUTION, findingConsumer);
            }
        } else {
            verifyPluginDoesNotHaveProperty(plugin, REPORT_INTEGRATION_EXECUTION, findingConsumer);
            verifyPluginDoesNotHaveProperty(plugin, MERGE_EXECUTION, findingConsumer);
            verifyPluginDoesNotHaveProperty(plugin, REPORT_IT_WITH_UDF_EXECUTION, findingConsumer);
            verifyPluginDoesNotHaveProperty(plugin, PREPARE_IT_AGENT_EXECUTION, findingConsumer);
        }
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
