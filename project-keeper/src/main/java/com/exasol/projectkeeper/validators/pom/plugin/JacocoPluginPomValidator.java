package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

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
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
