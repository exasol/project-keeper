package com.exasol.projectkeeper.validators.finding;

import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.Logger;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FindingsFixer {

    /**
     * Fix the given findings if they contain a fix.
     * 
     * @param findings list of validation findings
     * @param logger   logger to log to
     * @return flat list of findings that do not contain a fix
     */
    public List<SimpleValidationFinding> fixFindings(final List<ValidationFinding> findings, final Logger logger) {
        final FixingVisitor visitor = new FixingVisitor(logger);
        for (final ValidationFinding finding : findings) {
            finding.accept(visitor);
        }
        return visitor.getUnfixed();
    }

    @RequiredArgsConstructor
    private static class FixingVisitor implements ValidationFinding.Visitor {
        private final Logger logger;
        @Getter
        private final List<SimpleValidationFinding> unfixed = new ArrayList<>();

        @Override
        public void visit(final SimpleValidationFinding finding) {
            if (finding.hasFix()) {
                finding.getFix().fixError(this.logger);
            } else {
                this.unfixed.add(finding);
            }
        }

        @Override
        public void visit(final ValidationFindingGroup finding) {
            for (final ValidationFinding eachFinding : finding.getFindings()) {
                eachFinding.accept(this);
            }
            finding.getPostFix().run();
        }
    }
}
