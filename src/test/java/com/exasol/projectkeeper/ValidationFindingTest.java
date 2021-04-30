package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ValidationFindingTest {

    @Test
    void testWithFix() {
        final ValidationFinding finding = ValidationFinding.withMessage("").andFix((log) -> {
        }).build();
        assertThat(finding.hasFix(), equalTo(true));
    }

    @Test
    void testWithoutFix() {
        final ValidationFinding finding = ValidationFinding.withMessage("").build();
        assertThat(finding.hasFix(), equalTo(false));
    }
}