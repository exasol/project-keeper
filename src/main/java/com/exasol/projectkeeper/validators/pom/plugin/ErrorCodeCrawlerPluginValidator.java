package com.exasol.projectkeeper.validators.pom.plugin;

import java.util.Collection;
import java.util.function.Consumer;

import org.w3c.dom.*;

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
        if (enabledModules.contains(ProjectKeeperModule.LOMBOK)) {
            verifyLombokSourcePath(plugin, findingConsumer);
        } else {
            verifyPluginDoesNotHaveProperty(plugin, "configuration/sourcePaths", findingConsumer);
        }
    }

    private void verifyLombokSourcePath(final Node plugin, final Consumer<ValidationFinding> findingConsumer) {
        final Document document = plugin.getOwnerDocument();
        final Element sourcePaths = document.createElement("sourcePaths");
        sourcePaths.appendChild(createSourcePathElement(document, "target/delombok/main"));
        sourcePaths.appendChild(createSourcePathElement(document, "target/delombok/test"));
        verifyPluginPropertyHasExactValue(plugin, "configuration/sourcePaths", sourcePaths, findingConsumer);
    }

    private Element createSourcePathElement(final Document document, final String sourcePath) {
        final Element sourcePathElement = document.createElement("sourcePath");
        sourcePathElement.setTextContent(sourcePath);
        return sourcePathElement;
    }
}
