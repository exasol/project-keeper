package com.exasol.projectkeeper.validators.pom.dependencies;

import java.util.function.Consumer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * Validator for the dependency to jacoco.agent.
 */
public class JacocoAgentDependencyValidator extends AbstractDependencyValidator {

    /**
     * Create a new instance of {@link JacocoAgentDependencyValidator}.
     */
    public JacocoAgentDependencyValidator() {
        super("org.jacoco", "org.jacoco.agent", "0.8.5", Scope.RUNTIME);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.UDF_COVERAGE;
    }

    @Override
    protected void validateDetails(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {
        validateClassifier(dependency, findingConsumer);
    }

    private void validateClassifier(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {
        final Document pom = dependency.getOwnerDocument();
        final Element classifier = pom.createElement("classifier");
        classifier.appendChild(pom.createTextNode("runtime"));
        validateDependencyHasProperty(classifier, dependency, findingConsumer);
    }
}
