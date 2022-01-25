package com.exasol.projectkeeper;

import java.util.List;

import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Interface for project validators.
 */
// [impl->dsn~validators~1]
public interface Validator {

    /**
     * Validate the current project.
     * 
     * @return list of findings
     */
    public List<ValidationFinding> validate();
}
