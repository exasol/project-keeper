package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for maven-javadoc-plugin.
 */
public class JavadocPluginValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link JavadocPluginValidator}.
     */
    public JavadocPluginValidator() {
        super("maven_templates/maven-javadoc-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.MAVEN_CENTRAL;
    }

    @Override
    protected void validatePluginConfiguration(final Node plugin, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        verifyPluginPropertyHasExactValue(plugin, "executions", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/charset", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/doclint", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/serialwarn", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/failOnError", findingConsumer);
        verifyPluginPropertyHasExactValue(plugin, "configuration/failOnWarnings", findingConsumer);
    }
}
