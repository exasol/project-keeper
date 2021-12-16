package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the maven-surefire-plugin's configuration.
 */
public class SurefirePluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link SurefirePluginPomValidator}.
     */
    public SurefirePluginPomValidator() {
        super("maven_templates/maven-surefire-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "configuration/argLine", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
