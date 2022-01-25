package com.exasol.projectkeeper.validators.finding;

import java.util.List;

import lombok.Data;

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
