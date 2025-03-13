package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.validators.changesfile.ChangesFileName;

class ChangelogFileGeneratorTest {
    @Test
    void generate() {
        assertThat(new ChangelogFileGenerator().generate(files("1.0.1")),
                equalTo("# Changes\n\n* [1.0.1](changes_1.0.1.md)\n".replace("\n", System.lineSeparator())));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1.0-rc1", "1.0", "1" })
    void testNonStandardVersionFormats(final String version) {
        assertThat(new ChangelogFileGenerator().generate(files(version)), containsString(version));
    }

    private List<ChangesFileName> files(final String... versions) {
        return Arrays.stream(versions).map(ChangesFileName::new).toList();
    }
}
