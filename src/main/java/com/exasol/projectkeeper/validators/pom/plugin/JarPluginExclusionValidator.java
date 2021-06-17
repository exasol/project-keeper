package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for exclusion of the maven-jar-plugin.
 */
public class JarPluginExclusionValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link JarPluginExclusionValidator}.
     */
    public JarPluginExclusionValidator() {
        super("maven_templates/maven-jar-plugin-exclusion.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }
}
