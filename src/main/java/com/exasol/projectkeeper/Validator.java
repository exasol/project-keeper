package com.exasol.projectkeeper;

import java.util.function.Consumer;

/**
 * Interface for project validators.
 */
public interface Validator {

    /**
     * Validate the current project.
     * 
     * @param findingConsumer consumer to report the findings to
     * @return self for fluent programming
     */
    public Validator validate(final Consumer<ValidationFinding> findingConsumer);
}
