package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the artifact-reference-checker-maven-plugin's configuration.
 */
public class ArtifactReferenceCheckerPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link ArtifactReferenceCheckerPluginPomValidator}.
     */
    public ArtifactReferenceCheckerPluginPomValidator() {
        super("maven_templates/artifact-reference-checker-maven-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.JAR_ARTIFACT;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        super.validatePluginConfiguration(plugin, enabledModules, findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
    }
}
