package com.exasol.projectkeeper.validators.finding;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * This class ungroups all {@link ValidationFindingGroup}s into one flat list.
 */
public class FindingsUngrouper {

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
        @Getter
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
    }
}
