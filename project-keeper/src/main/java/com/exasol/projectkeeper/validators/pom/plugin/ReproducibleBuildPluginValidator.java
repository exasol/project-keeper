package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for reproducible-build-maven-plugin.
 */
public class ReproducibleBuildPluginValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link ReproducibleBuildPluginValidator}.
     */
    public ReproducibleBuildPluginValidator() {
        super("maven_templates/reproducible-build-maven-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }
}
