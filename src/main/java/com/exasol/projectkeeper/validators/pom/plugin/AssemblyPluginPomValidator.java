package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the maven-assembly-plugin's configuration.
 */
public class AssemblyPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link AssemblyPluginPomValidator}.
     */
    public AssemblyPluginPomValidator() {
        super("maven_templates/maven-assembly-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/descriptors/descriptor", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
        verifyPluginHasProperty(plugin, "configuration/finalName", findingConsumer);
    }
}
