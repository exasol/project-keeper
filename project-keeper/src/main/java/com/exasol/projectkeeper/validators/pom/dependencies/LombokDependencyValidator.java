package com.exasol.projectkeeper.validators.pom.dependencies;

import static com.exasol.projectkeeper.ProjectKeeperModule.LOMBOK;

import java.util.function.Consumer;

import org.w3c.dom.Node;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;

/**
 * This class verifies that projects using lombok have a dependency for lombok.
 */
public class LombokDependencyValidator extends AbstractDependencyValidator {
    /**
     * Create a new instance of {@link LombokDependencyValidator}.
     */
    public LombokDependencyValidator() {
        super("org.projectlombok", "lombok", "1.18.20", Scope.PROVIDED);
    }

    @Override
    public ProjectKeeperModule getModule() {
        return LOMBOK;
    }

    @Override
    protected void validateDetails(final Node dependency, final Consumer<ValidationFinding> findingConsumer) {
        // no additional validations
    }
}
