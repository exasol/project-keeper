package com.exasol.projectkeeper.validators.release;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;

import java.nio.file.Path;
import java.time.*;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

class ChangesFileReleaseValidatorTest {

    private static final Path PATH = Path.of("changes.md");
    private static final Instant NOW = Instant.parse("2007-12-03T10:15:30.00Z");

    // [utest->dsn~verify-release-mode.verify-release-date~1]
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "invalid, E-PK-CORE-182: Release date 'invalid' has invalid format in 'changes.md'",
            "2007-??-??, E-PK-CORE-182: Release date '2007-??-??' has invalid format in 'changes.md'",
            "2007-02-??, E-PK-CORE-182: Release date '2007-02-??' has invalid format in 'changes.md'",
            "2007-12-02, E-PK-CORE-183: Release date 2007-12-02 must be today 2007-12-03 in 'changes.md'",
            "2007-12-04, E-PK-CORE-183: Release date 2007-12-04 must be today 2007-12-03 in 'changes.md'",
            "2007-12-03, NULL" })
    void invalidReleaseDate(final String releaseDate, final String expectedError) {
        final List<String> findings = findings(ChangesFile.builder().releaseDate(releaseDate));
        if (expectedError == null) {
            assertThat(findings, emptyIterable());
        } else {
            assertThat(findings, contains(expectedError));
        }
    }

    private List<String> findings(final ChangesFile.Builder changesFile) {
        return testee(changesFile).validate().stream() //
                .map(SimpleValidationFinding.class::cast) //
                .map(SimpleValidationFinding::getMessage) //
                .toList();
    }

    private ChangesFileReleaseValidator testee(final ChangesFile.Builder changesFile) {
        return new ChangesFileReleaseValidator(PATH, changesFile.build(), Clock.fixed(NOW, ZoneId.of("UTC")));
    }
}
