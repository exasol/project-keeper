package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExasolVersionMatcherTest {

    private static final ExasolVersionMatcher EXASOL_VERSION_MATCHER = new ExasolVersionMatcher();

    @ParameterizedTest
    @CsvSource({ //
            "0.1.0, true", //
            "0.100.0, true", //
            "10.10.123, true", //
            "10.10, false", //
            "1.1.1.1, false", //
            "1.1.1-1, false", //
            "1.1.1a, false",//
    })
    void testMatching(final String input, final boolean expectedResult) {
        assertThat(EXASOL_VERSION_MATCHER.isExasolStyleVersion(input), equalTo(expectedResult));
    }
}