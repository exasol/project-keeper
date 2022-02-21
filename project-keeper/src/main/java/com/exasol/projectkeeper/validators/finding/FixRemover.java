package com.exasol.projectkeeper.validators.finding;

import java.util.List;

/**
 * This class removes the fixes from a list of validation findings.
 */
public class FixRemover {

    /**
     * Remove the fixes from a list of validation findings.
     * 
     * @return list of validation findings without fixes
     */
    public List<ValidationFinding> removeFixes(final List<? extends ValidationFinding> findings) {
        return new FindingModifier(finding -> SimpleValidationFinding.withMessage(finding.getMessage()).build())
                .modifyAll(findings);
    }
}
