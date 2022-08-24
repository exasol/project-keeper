package com.exasol.projectkeeper.validators.finding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.junit.jupiter.api.Test;

class FindingFilterTest {
    private static final SimpleValidationFinding FINDING_1 = SimpleValidationFinding.withMessage("message 1").build();
    private static final SimpleValidationFinding FINDING_2 = SimpleValidationFinding.withMessage("message 2").build();
    private static final SimpleValidationFinding FINDING_3 = SimpleValidationFinding.withMessage("message 3").build();
    private static final SimpleValidationFinding FINDING_3_MULTILINE = SimpleValidationFinding
            .withMessage("message 3\nnext line").build();
    private static final SimpleValidationFinding OTHER = SimpleValidationFinding.withMessage("other").build();
    private static final Runnable POST_FIX = () -> {
    };
    private static final ValidationFindingGroup FINDING_GROUP = new ValidationFindingGroup(
            List.of(FINDING_3, FINDING_3_MULTILINE, OTHER), POST_FIX);

    @Test
    void testFilterSimpleFinding() {
        final List<ValidationFinding> result = new FindingFilter(List.of("\\Qmessage 1\\E"))
                .filterFindings(List.of(FINDING_1, FINDING_2));
        assertThat(result, contains(FINDING_2));
    }

    @Test
    void testFilterByRegex() {
        final List<ValidationFinding> result = new FindingFilter(List.of("message .*"))
                .filterFindings(List.of(FINDING_1, FINDING_2, FINDING_3_MULTILINE, OTHER));
        assertThat(result, contains(OTHER));
    }

    @Test
    void testFilterFindingGroup() {
        final List<ValidationFinding> result = new FindingFilter(List.of("\\Qmessage 3\\E"))
                .filterFindings(List.of(FINDING_GROUP));
        assertThat(result, contains(new ValidationFindingGroup(List.of(FINDING_3_MULTILINE, OTHER), POST_FIX)));
    }

    @Test
    void testFilterAllFromFindingGroup() {
        final List<ValidationFinding> result = new FindingFilter(List.of(".*")).filterFindings(List.of(FINDING_GROUP));
        assertThat(result, empty());
    }
}