package com.exasol.projectkeeper;

import java.util.List;

/**
 * Interface for project validators.
 */
public interface Validator {

    /**
     * Validate the current project.
     * 
     * @return list of findings
     */
    public List<ValidationFinding> validate();
}
