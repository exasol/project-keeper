package com.exasol.projectkeeper.validators.finding;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class FindingsUngrouperTest {

    private static final SimpleValidationFinding FINDING_1 = SimpleValidationFinding.withMessage("my finding 1")
            .build();
    private static final SimpleValidationFinding FINDING_2 = SimpleValidationFinding.withMessage("my finding 2")
            .build();
    private static final SimpleValidationFinding FINDING_3 = SimpleValidationFinding.withMessage("my finding").build();

    @Test
    void testUngroup() {
        final List<ValidationFinding> findings = List.of(FINDING_1,
                new ValidationFindingGroup(List.of(FINDING_2, FINDING_3), () -> {
                }));
        final List<SimpleValidationFinding> ungroupFindings = new FindingsUngrouper().ungroupFindings(findings);
        assertThat(ungroupFindings, Matchers.contains(FINDING_1, FINDING_2, FINDING_3));
    }
}