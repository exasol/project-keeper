package com.exasol.projectkeeper.validators.finding;

import java.util.ArrayList;
import java.util.List;

/**
 * This class ungroups all {@link ValidationFindingGroup}s into one flat list.
 */
public class FindingsUngrouper {
    /** Create a new instance */
    public FindingsUngrouper() {
        // Empty constructor required by javadoc
    }

    /**
     * Ungroup all {@link ValidationFindingGroup}s into one flat list.
     * <p>
     * Don't run fix on the ungrouped findings! It will skip the postFix part.
     * </p>
     *
     * @param findings findings to ungroup
     * @return flat list of findings
     */
    public List<SimpleValidationFinding> ungroupFindings(final List<ValidationFinding> findings) {
        final UngoupingVisitor ungoupingVisitor = new UngoupingVisitor();
        for (final ValidationFinding finding : findings) {
            finding.accept(ungoupingVisitor);
        }
        return ungoupingVisitor.getResult();
    }

    private static class UngoupingVisitor implements ValidationFinding.Visitor {
        private final List<SimpleValidationFinding> result = new ArrayList<>();

        @Override
        public void visit(final SimpleValidationFinding finding) {
            this.result.add(finding);
        }

        @Override
        public void visit(final ValidationFindingGroup finding) {
            for (final ValidationFinding eachFinding : finding.getFindings()) {
                eachFinding.accept(this);
            }
        }

        /**
         * @return list of findings representing the result of the validation
         */
        public List<SimpleValidationFinding> getResult() {
            return this.result;
        }
    }
}
