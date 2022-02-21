package com.exasol.projectkeeper.validators.finding;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class FindingModifier {
    private final Modifier modifier;

    /**
     * Create a new instance of {@link FindingModifier}.
     * 
     * @param modifier modifier
     */
    public FindingModifier(final Modifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Apply the given modifier to all findings in a list also to nested one's in {@link ValidationFindingGroup}s.
     *
     * @return modified findings
     */
    public List<ValidationFinding> modifyAll(final List<? extends ValidationFinding> findings) {
        return findings.stream().map(this::apply).collect(Collectors.toList());
    }

    private ValidationFinding apply(final ValidationFinding finding) {
        final ModifyingVisitor visitor = new ModifyingVisitor(this.modifier);
        finding.accept(visitor);
        return visitor.getResult();
    }

    /**
     * Functional interface for a modifier.
     */
    @FunctionalInterface
    public interface Modifier {
        /**
         * Modify a given {@link SimpleValidationFinding}.
         *
         * @param original original finding to modify
         * @return modified finding
         */
        SimpleValidationFinding modify(SimpleValidationFinding original);
    }

    @RequiredArgsConstructor
    private static class ModifyingVisitor implements ValidationFinding.Visitor {
        private final Modifier modifier;
        @Getter
        private ValidationFinding result;

        @Override
        public void visit(final SimpleValidationFinding finding) {
            this.result = this.modifier.modify(finding);
        }

        @Override
        public void visit(final ValidationFindingGroup finding) {
            final List<ValidationFinding> modifiedFindings = finding.getFindings().stream().map(this::apply)
                    .collect(Collectors.toList());
            this.result = new ValidationFindingGroup(modifiedFindings, finding.getPostFix());
        }

        private ValidationFinding apply(final ValidationFinding finding) {
            final ModifyingVisitor visitor = new ModifyingVisitor(this.modifier);
            finding.accept(visitor);
            return visitor.getResult();
        }
    }
}
