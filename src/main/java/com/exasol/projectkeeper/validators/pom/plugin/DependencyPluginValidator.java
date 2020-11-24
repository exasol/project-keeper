package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the maven-dependency-plugin.
 */
public class DependencyPluginValidator extends AbstractPluginPomValidator {
    public DependencyPluginValidator() {
        super("maven_templates/maven-dependency-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.UDF_COVERAGE;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions/execution[id/text() = 'copy-jacoco']", findingConsumer);
    }
}
