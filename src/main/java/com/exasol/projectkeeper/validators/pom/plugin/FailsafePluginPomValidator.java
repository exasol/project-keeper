package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the maven-failsafe-plugin's configuration.
 */
public class FailsafePluginPomValidator extends AbstractPluginPomValidator {
    private static final String TEST_COVERAGE_CONFIGURATION = "configuration/systemPropertyVariables/test.coverage";

    /**
     * Create a new instance of {@link FailsafePluginPomValidator}.
     */
    public FailsafePluginPomValidator() {
        super("maven_templates/maven-failsafe-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/argLine", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
        if (enabledModules.contains(ProjectKeeperModule.UDF_COVERAGE)) {
            verifyPluginPropertyHasExactValue(plugin, TEST_COVERAGE_CONFIGURATION, findingConsumer);
        } else {
            verifyPluginDoesNotHaveProperty(plugin, TEST_COVERAGE_CONFIGURATION, findingConsumer);
        }
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.INTEGRATION_TESTS;
    }
}
