package com.exasol.projectkeeper.validators.finding;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

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
        return findings.stream().map(this::removeFixFromFinding).collect(Collectors.toList());
    }

    private ValidationFinding removeFixFromFinding(final ValidationFinding finding) {
        final FixRemoveVisitor visitor = new FixRemoveVisitor();
        finding.accept(visitor);
        return visitor.getResult();
    }

    private static class FixRemoveVisitor implements ValidationFinding.Visitor {
        @Getter
        private ValidationFinding result;

        @Override
        public void visit(final SimpleValidationFinding finding) {
            this.result = SimpleValidationFinding.withMessage(finding.getMessage()).build();
        }

        @Override
        public void visit(final ValidationFindingGroup finding) {
            final List<ValidationFinding> findingsWithoutFixes = new FixRemover().removeFixes(finding.getFindings());
            this.result = new ValidationFindingGroup(findingsWithoutFixes, finding.getPostFix());
        }
    }
}
