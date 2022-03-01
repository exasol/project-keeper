package com.exasol.projectkeeper.validators.finding;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class is a filter for {@link ValidationFinding}s.
 */
public class FindingFilter {
    private final List<Pattern> denyListPatterns;

    /**
     * Create a new instance.
     *
     * @param denyList regular expressions for finding messages to exclude
     */
    public FindingFilter(final List<String> denyList) {
        this.denyListPatterns = denyList.stream().map(Pattern::compile).collect(Collectors.toList());
    }

    /**
     * Filter a list of validation findings by a list of regular expressions that match findings messages to exclude.
     * <p>
     * This filter recursively filters {@link ValidationFindingGroup}s.
     * </p>
     * 
     * @param findings findings to filter
     * @return filtered list of findings
     */
    public List<ValidationFinding> filterFindings(final List<ValidationFinding> findings) {
        final List<ValidationFinding> result = new ArrayList<>();
        for (final ValidationFinding finding : findings) {
            final FilteringVisitor visitor = new FilteringVisitor(this.denyListPatterns);
            finding.accept(visitor);
            visitor.getResult().ifPresent(result::add);
        }
        return result;
    }

    @RequiredArgsConstructor
    private static class FilteringVisitor implements ValidationFinding.Visitor {
        private final List<Pattern> denyList;
        @Getter
        private Optional<ValidationFinding> result;

        @Override
        public void visit(final SimpleValidationFinding finding) {
            if (!isExcluded(finding)) {
                this.result = Optional.of(finding);
            } else {
                this.result = Optional.empty();
            }
        }

        private boolean isExcluded(final SimpleValidationFinding finding) {
            for (final Pattern denyListPattern : this.denyList) {
                if (denyListPattern.matcher(finding.getMessage()).matches()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public void visit(final ValidationFindingGroup finding) {
            final List<? extends ValidationFinding> nestedFindings = finding.getFindings();
            final List<ValidationFinding> filteredNestedFindings = new ArrayList<>(nestedFindings.size());
            for (final ValidationFinding eachFinding : nestedFindings) {
                final FilteringVisitor visitor = new FilteringVisitor(this.denyList);
                eachFinding.accept(visitor);
                visitor.getResult().ifPresent(filteredNestedFindings::add);
            }
            if (filteredNestedFindings.isEmpty()) {
                this.result = Optional.empty();
            } else {
                this.result = Optional.of(new ValidationFindingGroup(filteredNestedFindings, finding.getPostFix()));
            }
        }
    }
}
