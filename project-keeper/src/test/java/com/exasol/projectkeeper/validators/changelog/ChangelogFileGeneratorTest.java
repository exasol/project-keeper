package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ChangelogFileGeneratorTest {
    private static final ChangelogFileGenerator GENERATOR = new ChangelogFileGenerator();

    @Test
    void testGenerate() {
        assertThat(GENERATOR.generate(List.of("1.0.1")),
                equalTo("# Changes\n\n* [1.0.1](changes_1.0.1.md)\n".replace("\n", System.lineSeparator())));
    }

    @Test
    void testOrder() {
        final String content = GENERATOR.generate(List.of("1.0.1", "1.0.0", "1.1.0", "0.1.0", "1.0.10"));
        assertThat(content, stringContainsInOrder("[1.1.0]", "[1.0.10]", "[1.0.1]", "[1.0.0]", "[0.1.0"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1.0-rc1", "1.0", "1" })
    void testNonStandardVersionFormats(final String version) {
        assertThat(GENERATOR.generate(List.of(version)), containsString(version));
    }
}