package com.exasol.projectkeeper.validators.finding;

import java.util.List;

/**
 * Group of validation findings with a callback to run after all fixes are applied.
 * 
 * @param findings findings to group
 * @param postFix  callback to invoke after fixing the contained findings
 */
public record ValidationFindingGroup(List<ValidationFinding> findings, Runnable postFix) implements ValidationFinding {

    /**
     * Get findings.
     * 
     * @return findings to group
     */
    public List<ValidationFinding> getFindings() {
        return findings;
    }

    /**
     * Get post fix callback.
     * 
     * @return callback to invoke after fixing the contained findings
     */
    public Runnable getPostFix() {
        return postFix;
    }

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
