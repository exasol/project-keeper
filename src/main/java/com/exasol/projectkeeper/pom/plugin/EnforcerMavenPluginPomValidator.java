package com.exasol.projectkeeper.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.pom.PomValidationFinding;

/**
 * Validator for the maven-enforcer-plugin's configuration.
 */
public class EnforcerMavenPluginPomValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link EnforcerMavenPluginPomValidator}.
     */
    public EnforcerMavenPluginPomValidator() {
        super("maven_templates/maven-enforcer-plugin.xml");
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<PomValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions[execution/id/text() = 'enforce-maven']", findingConsumer);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
