package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for error-code-crawler-maven-plugin.
 */
public class ErrorCodeCrawlerPluginValidator extends AbstractPluginPomValidator {

    /**
     * Create a new instance of {@link ErrorCodeCrawlerPluginValidator}.
     */
    public ErrorCodeCrawlerPluginValidator() {
        super("maven_templates/error-code-crawler-maven-plugin.xml");
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
