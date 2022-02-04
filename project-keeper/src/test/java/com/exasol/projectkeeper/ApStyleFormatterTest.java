package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ApStyleFormatterTest {

    @ParameterizedTest
    @CsvSource({ //
            "my headline, My Headline", //
            "test for a headline, Test for a Headline", //
            "I like abbreviations like JDBC, I Like Abbreviations Like JDBC"//
    })
    void testFormatting(final String input, final String expectedOutput) {
        assertThat(ApStyleFormatter.capitalizeApStyle(input), equalTo(expectedOutput));
    }
}