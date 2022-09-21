package com.exasol.projectkeeper.validators.finding;

import java.util.ArrayList;
import java.util.List;

import com.exasol.projectkeeper.Logger;

import lombok.RequiredArgsConstructor;

/**
 * This class runs the {@link SimpleValidationFinding.Fix} callbacks of {@link ValidationFinding}s.
 */
public class FindingsFixer {
    private final Logger logger;

    /**
     * Create a new instance of {@link FindingsFixer}.
     *
     * @param logger logger to log to
     */
    public FindingsFixer(final Logger logger) {
        this.logger = logger;
    }

    /**
     * Fix the given findings if they contain a fix.
     *
     * @param findings list of validation findings
     * @return flat list of findings that do not contain a fix
     */
    public List<SimpleValidationFinding> fixFindings(final List<ValidationFinding> findings) {
        final FixingVisitor visitor = new FixingVisitor(this.logger);
        for (final ValidationFinding finding : findings) {
            finding.accept(visitor);
        }
        return visitor.getUnfixed();
    }

    @RequiredArgsConstructor
    private static class FixingVisitor implements ValidationFinding.Visitor {
        private final Logger logger;
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

        /**
         * @return list of findings that have not been fixed, yet
         */
        public List<SimpleValidationFinding> getUnfixed() {
            return this.unfixed;
        }
    }
}
