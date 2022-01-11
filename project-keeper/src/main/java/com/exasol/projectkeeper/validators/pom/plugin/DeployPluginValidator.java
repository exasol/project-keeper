package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for maven-deploy-plugin.
 */
public class DeployPluginValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link DependencyPluginValidator}.
     */
    public DeployPluginValidator() {
        super("maven_templates/maven-deploy-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.MAVEN_CENTRAL;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/skip", findingConsumer);
    }
}
