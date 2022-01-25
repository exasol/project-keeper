package com.exasol.projectkeeper.validators.finding;

import java.util.List;

import lombok.Data;

/**
 * Group of validation findings with a callback to run after all fixes are applied.
 */
@Data
public class ValidationFindingGroup implements ValidationFinding {
    /** Findings to group */
    private final List<? extends ValidationFinding> findings;
    /** Callback to invoke after fixing the contained findings */
    private final Runnable postFix;

    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);
    }
}
