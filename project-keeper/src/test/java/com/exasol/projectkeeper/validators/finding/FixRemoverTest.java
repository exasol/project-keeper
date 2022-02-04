package com.exasol.projectkeeper.validators.finding;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class FixRemoverTest {

    @Test
    void test() {
        final SimpleValidationFinding finding = SimpleValidationFinding.withMessage("test").andFix(log -> {
        }).build();
        final List<ValidationFinding> result = new FixRemover().removeFixes(List.of(finding));
        assertThat(result, Matchers.contains(SimpleValidationFinding.withMessage("test").build()));
    }
}