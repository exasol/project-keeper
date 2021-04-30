package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.xpath.XPathErrorHandlingWrapper.runXPath;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;
import org.w3c.dom.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.validators.pom.AbstractPomValidator;
import com.exasol.projectkeeper.validators.pom.PomValidator;

/**
 * Abstract basis for {@link PomValidator}s that validate pom dependencies.
 */
@SuppressWarnings("java:S1192") // parameter DEPENDENCY for error reporting can not be replaced by a constant
public abstract class AbstractDependencyValidator extends AbstractPomValidator implements PomValidator {
    @SuppressWarnings("java:S1075") // not a configurable URL
    private static final String DEPENDENCIES_XPATH = "/project/dependencies";
    private final String groupId;
    private final String artifactId;
    private final String version;
    private final Scope scope;

    protected AbstractDependencyValidator(final String groupId, final String artifactId, final String version,
            final Scope scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }

    @Override
    public void validate(final Document pom, final Collection<ProjectKeeperModule> enabledModules,
            final Consumer<ValidationFinding> findingConsumer) {
        final Node dependency = runXPath(pom, DEPENDENCIES_XPATH + "/dependency[artifactId/text() = '" + this.artifactId
                + "' and groupId/text() = '" + this.groupId + "']");
        if (dependency == null) {
            findingConsumer.accept(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-29").message("Missing dependency {{DEPENDENCY}}.")
                            .parameter("DEPENDENCY", this.groupId + ":" + this.artifactId).toString())
                    .andFix(getFix(pom)).build());
        } else {
            validateDependency(dependency, findingConsumer);
        }
    }

    private void validateDependency(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {
        validateDetails(dependency, findingConsumer);
        validateScope(dependency, findingConsumer);
    }

    private ValidationFinding.Fix getFix(final Document pom) {
        return log -> {
            createObjectPathIfNotExists(runXPath(pom, "/project"), List.of("dependencies"));
            final Node dependencies = runXPath(pom, DEPENDENCIES_XPATH);
            final Element dependencyTemplate = createTemplate(pom, log);
            dependencies.appendChild(dependencyTemplate);
        };
    }

    protected abstract void validateDetails(final Node dependency, final Consumer<ValidationFinding> findingConsumer);

    private void validateScope(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {
        final var pom = dependency.getOwnerDocument();
        final var expectedScope = pom.createElement("scope");
        expectedScope.appendChild(pom.createTextNode(this.scope.name().toLowerCase()));
        validateDependencyHasProperty(expectedScope, dependency, findingConsumer);
    }

    private Element createTemplate(final Document pom, final Log log) {
        final var dependencyTemplate = pom.createElement("dependency");
        final var artifactIdField = pom.createElement("artifactId");
        artifactIdField.appendChild(pom.createTextNode(this.artifactId));
        dependencyTemplate.appendChild(artifactIdField);
        final var groupIdField = pom.createElement("groupId");
        groupIdField.appendChild(pom.createTextNode(this.groupId));
        dependencyTemplate.appendChild(groupIdField);
        final var versionField = pom.createElement("version");
        versionField.appendChild(pom.createTextNode(this.version));
        dependencyTemplate.appendChild(versionField);
        validateDependency(dependencyTemplate, finding -> finding.getFix().fixError(log));
        return dependencyTemplate;
    }

    protected void validateDependencyHasProperty(final Element expectedProperty, final Node dependency,
            final Consumer<ValidationFinding> findingConsumer) {
        final Node property = runXPath(dependency, expectedProperty.getTagName());
        if (property == null) {
            findingConsumer.accept(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-30")
                            .message("Missing property {{PROPERTY}} in dependency {{DEPENDENCY}}.")
                            .parameter("PROPERTY", expectedProperty.getTagName())
                            .parameter("DEPENDENCY", this.groupId + ":" + this.artifactId).toString())
                    .andFix(getMissingPropertyFix(expectedProperty, dependency)).build());
        } else {
            if (!isXmlEqual(property, expectedProperty)) {
                findingConsumer.accept(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-31")
                        .message("The property {{PROPERTY}} of the dependency {{DEPENDENCY}} has an illegal value.")
                        .parameter("PROPERTY", expectedProperty.getTagName())
                        .parameter("DEPENDENCY", this.groupId + ":" + this.artifactId).toString())
                        .andFix(getWrongPropertyValueFix(expectedProperty, dependency, property)).build());
            }
        }
    }

    private ValidationFinding.Fix getMissingPropertyFix(final Element expectedProperty, final Node dependency) {
        return log -> dependency.appendChild(dependency.getOwnerDocument().adoptNode(expectedProperty));
    }

    private ValidationFinding.Fix getWrongPropertyValueFix(final Element expectedProperty, final Node dependency,
            final Node property) {
        return log -> dependency.replaceChild(dependency.getOwnerDocument().adoptNode(expectedProperty), property);
    }

    enum Scope {
        COMPILE, PROVIDED, RUNTIME, TEST
    }

    @Override
    public boolean isExcluded(final Collection<String> excludedPlugins) {
        return false;
    }
}
