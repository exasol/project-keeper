package com.exasol.projectkeeper.validators.finding;

import java.util.List;
import java.util.Objects;

/**
 * Group of validation findings with a callback to run after all fixes are applied.
 */
public final class ValidationFindingGroup implements ValidationFinding {
    private final List<ValidationFinding> findings;
    private final Runnable postFix;

    /**
     * Create a new instance.
     * 
     * @param findings findings to group
     * @param postFix  callback to invoke after fixing the contained findings
     */
    public ValidationFindingGroup(final List<ValidationFinding> findings, final Runnable postFix) {
        this.findings = findings;
        this.postFix = postFix;
    }

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

    @Override
    public String toString() {
        return "ValidationFindingGroup [findings=" + findings + ", postFix=" + postFix + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(findings, postFix);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidationFindingGroup other = (ValidationFindingGroup) obj;
        return Objects.equals(findings, other.findings) && Objects.equals(postFix, other.postFix);
    }
}
