package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for maven-gpg-plugin.
 */
public class GpgPluginValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link GpgPluginValidator}.
     */
    public GpgPluginValidator() {
        super("maven_templates/maven-gpg-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.MAVEN_CENTRAL;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }
}
