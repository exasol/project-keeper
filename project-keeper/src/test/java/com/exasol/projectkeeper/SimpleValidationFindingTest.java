package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

import nl.jqno.equalsverifier.EqualsVerifier;

class SimpleValidationFindingTest {

    @Test
    void testWithFix() {
        final SimpleValidationFinding finding = SimpleValidationFinding.withMessage("").andFix((final Logger log) -> {
        }).build();
        assertThat(finding.hasFix(), equalTo(true));
    }

    @Test
    void testWithoutFix() {
        final SimpleValidationFinding finding = SimpleValidationFinding.withMessage("").build();
        assertThat(finding.hasFix(), equalTo(false));
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(SimpleValidationFinding.class).verify();
    }
}