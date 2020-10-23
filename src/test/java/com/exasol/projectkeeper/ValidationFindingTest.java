package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ValidationFindingTest {

    @Test
    void testWithFix() {
        final ValidationFinding finding = new ValidationFinding("", (log) -> {
        });
        assertThat(finding.hasFix(), equalTo(true));
    }

    @Test
    void testWithoutFix() {
        final ValidationFinding finding = new ValidationFinding("");
        assertThat(finding.hasFix(), equalTo(false));
    }
}