package com.exasol.projectkeeper.validators.release;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.time.*;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.validators.changesfile.ChangesFile;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileSection;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.release.github.GitHubAdapter;
import com.exasol.projectkeeper.validators.release.github.IssueState;

// [utest->dsn~verify-release-mode.verify-changes-file~1]
@ExtendWith(MockitoExtension.class)
class ChangesFileReleaseValidatorTest {
    private static final Path PATH = Path.of("changes.md");
    private static final Instant NOW = Instant.parse("2007-12-03T10:15:30.00Z");
    private static final String TODAY = "2007-12-03";
    private static final String CODE_NAME = "code name";

    @Mock
    GitHubAdapter gitHubAdapterMock;

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
        final List<String> findings = findings(ChangesFile.builder().releaseDate(releaseDate).codeName(CODE_NAME));
        if (expectedError == null) {
            assertThat(findings, emptyIterable());
        } else {
            assertThat(findings, contains(expectedError));
        }
    }

    @Test
    void closedIssuesNoIssuesMentioned() {
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME).releaseDate(TODAY));
        assertThat(findings, empty());
    }

    // [utest->dsn~verify-release-mode.verify-issues-closed~1]
    @ParameterizedTest
    @CsvSource({ "OPEN", "MISSING" })
    void closedIssuesIssueNotClosed(final IssueState state) {
        when(gitHubAdapterMock.getIssueState(42)).thenReturn(state);
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME)
                .addSection(ChangesFileSection.builder("## Bugfixes").addLine("- #42: Fixed a bug").build())
                .releaseDate(TODAY));
        assertThat(findings, contains(
                "E-PK-CORE-186: The following GitHub issues are marked as fixed in 'changes.md' but are not closed in GitHub: [42]"));
    }

    @Test
    void closedIssuesIssueClosed() {
        when(gitHubAdapterMock.getIssueState(42)).thenReturn(IssueState.CLOSED);
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME)
                .addSection(ChangesFileSection.builder("## Bugfixes").addLine("- #42: Fixed a bug").build())
                .releaseDate(TODAY));
        assertThat(findings, empty());
    }

    @Test
    void summarySectionEmpty() {
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME)
                .summary(ChangesFileSection.builder("## Summary").build()).releaseDate(TODAY));
        assertThat(findings,
                contains("E-PK-CORE-194: Section '## Summary' is empty in 'changes.md'. Add content to section."));
    }

    @Test
    void summarySectionBlank() {
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME)
                .summary(ChangesFileSection.builder("## Summary").addLine("  ").build()).releaseDate(TODAY));
        assertThat(findings,
                contains("E-PK-CORE-194: Section '## Summary' is empty in 'changes.md'. Add content to section."));
    }

    @Test
    void summarySectionNotBlank() {
        final List<String> findings = findings(ChangesFile.builder().codeName(CODE_NAME)
                .summary(ChangesFileSection.builder("## Summary").addLine("non-blank content").build())
                .releaseDate(TODAY));
        assertThat(findings, empty());
    }

    @Test
    void codeNameMissing() throws IOException {
        final List<String> findings = findings(ChangesFile.builder()
                .summary(ChangesFileSection.builder("## Summary").addLine("non-blank content").build())
                .releaseDate(TODAY));
        assertThat(findings, contains("E-PK-CORE-196: Code name in '" + PATH + "' is missing. Add a code name."));
    }

    @Test
    void codeNameBlank() throws IOException {
        final List<String> findings = findings(ChangesFile.builder()
                .summary(ChangesFileSection.builder("## Summary").addLine("non-blank content").build())
                .releaseDate(TODAY));
        assertThat(findings, contains("E-PK-CORE-196: Code name in '" + PATH + "' is missing. Add a code name."));
    }

    private List<String> findings(final ChangesFile.Builder changesFile) {
        return testee(changesFile).validate().stream() //
                .map(SimpleValidationFinding.class::cast) //
                .map(SimpleValidationFinding::getMessage) //
                .toList();
    }

    private ChangesFileReleaseValidator testee(final ChangesFile.Builder changesFile) {
        return new ChangesFileReleaseValidator(PATH, changesFile.build(), gitHubAdapterMock,
                Clock.fixed(NOW, ZoneId.of("UTC")));
    }
}
