package com.exasol.projectkeeper.dependencyupdate;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.validators.changesfile.*;
import com.exasol.projectkeeper.validators.changesfile.ChangesFile.Builder;

// [utest->dsn~dependency-updater.update-changelog~1]
@ExtendWith(MockitoExtension.class)
class ChangesFileUpdaterTest {

    private static final String VERSION = "1.2.3";
    private static final Path PROJECT_DIR = Path.of("projectDir");
    private static final Path CHANGES_FILE = PROJECT_DIR.resolve("doc/changes/").resolve("changes_" + VERSION + ".md");
    @Mock
    VulnerabilityInfoProvider vulnerabilityInfoProviderMock;
    @Mock
    ChangesFileIO changesFileIOMock;

    @Test
    void noChangesWhenNoVulnerabilitiesPresent() {
        assertNull(getUpdatedChanges(emptyList(), ChangesFile.builder().build()));
    }

    @Test
    void invalidIssueUrl() {
        final List<Vulnerability> vulnerabilities = List
                .of(new Vulnerability("cve", "cwe", "desc", "coord", null, "invalidUrl"));
        final ChangesFile inputChangesFile = ChangesFile.builder().build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> getUpdatedChanges(vulnerabilities, inputChangesFile));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-181: Issues URL 'invalidUrl' does not match expected pattern ^https://github.com/exasol/[^/]+/issues/(\\d+)$. "
                        + "This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    static Stream<Arguments> codeName() {
        return Stream.of( //
                testCase(null, List.of(vulnerability(1)), "Fixed vulnerability cve-1 in coordinates-1"), //
                testCase("", List.of(vulnerability(1)), "Fixed vulnerability cve-1 in coordinates-1"), //
                testCase(null, List.of(vulnerability(1), vulnerability(2)), "Fixed vulnerabilities cve-1, cve-2"), //
                testCase("", List.of(vulnerability(1), vulnerability(2)), "Fixed vulnerabilities cve-1, cve-2"), //
                testCase("Existing Text", List.of(vulnerability(1)),
                        "Existing Text, fixed vulnerability cve-1 in coordinates-1"), //
                testCase("Existing Text", List.of(vulnerability(1), vulnerability(2)),
                        "Existing Text, fixed vulnerabilities cve-1, cve-2") //
        );
    }

    @ParameterizedTest
    @MethodSource("codeName")
    void codeNameUpdated(final TestCase<String> test) {
        final ChangesFile changes = ChangesFile.builder().codeName(test.currentText).build();
        assertThat(getUpdatedChanges(test.vulnerabilities, changes).getCodeName(), equalTo(test.expected));
    }

    static Stream<Arguments> summary() {
        return Stream.of( //
                testCase(null, List.of(vulnerability(1)),
                        List.of("", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "")), //
                testCase(emptyList(), List.of(vulnerability(1)),
                        List.of("", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "")), //
                testCase(List.of("existing", "summary"), List.of(vulnerability(1)),
                        List.of("existing", "summary", "", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "")), //
                testCase(null, List.of(vulnerability(1, emptyList())),
                        List.of("", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "")), //
                testCase(null, List.of(vulnerability(1, List.of("link-1"))),
                        List.of("", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "#### References",
                                "* link-1", "")), //
                testCase(null, List.of(vulnerability(1, List.of("link-1", "link-2"))),
                        List.of("", "This release fixes the following vulnerability:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "#### References",
                                "* link-1", "* link-2", "")), //
                testCase(null, List.of(vulnerability(1), vulnerability(2)),
                        List.of("", "This release fixes the following 2 vulnerabilities:", "",
                                "### cve-1 (cwe-1) in dependency `coordinates-1`", "description-1", "", //
                                "### cve-2 (cwe-2) in dependency `coordinates-2`", "description-2", "")) //
        );
    }

    @ParameterizedTest
    @MethodSource("summary")
    void summaryUpdated(final TestCase<List<String>> test) {
        final ChangesFile changes = ChangesFile.builder().summary(test.currentText == null ? null
                : ChangesFileSection.builder("## Summary").addLines(test.currentText).build()).build();
        final List<String> updatedSummary = getUpdatedChanges(test.vulnerabilities, changes).getSummarySection() //
                .orElseThrow().getContent();
        assertThat(updatedSummary, equalTo(test.expected));
    }

    static Stream<Arguments> securitySection() {
        final ChangesFileSection featureSection = section("## Features", "* Feature 1");
        final ChangesFileSection bugfixSection = section("## Bugfixes", "* Bugfix 1");
        final ChangesFileSection existingSecuritySection = securitySection("* Existing security fix");
        return Stream.of( //
                testCaseSecurity(null, List.of(vulnerability(1)),
                        List.of(securitySection("", "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`",
                                ""))),
                testCaseSecurity(null, List.of(vulnerability(1), vulnerability(2)),
                        List.of(securitySection("", "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`",
                                "* #2: Fixed vulnerability cve-2 in dependency `coordinates-2`", ""))),
                testCaseSecurity(emptyList(), List.of(vulnerability(1)),
                        List.of(securitySection("", "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`",
                                ""))),
                testCaseSecurity(List.of(featureSection), List.of(vulnerability(1)),
                        List.of(securitySection("", "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`",
                                ""), featureSection)),
                testCaseSecurity(List.of(featureSection, bugfixSection), List.of(vulnerability(1)),
                        List.of(securitySection("", "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`",
                                ""), featureSection, bugfixSection)),
                testCaseSecurity(List.of(featureSection, existingSecuritySection, bugfixSection),
                        List.of(vulnerability(1)),
                        List.of(securitySection("* Existing security fix",
                                "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`", ""), featureSection,
                                bugfixSection)),
                testCaseSecurity(List.of(existingSecuritySection), List.of(vulnerability(1)),
                        List.of(securitySection("* Existing security fix",
                                "* #1: Fixed vulnerability cve-1 in dependency `coordinates-1`", ""))));
    }

    @ParameterizedTest
    @MethodSource("securitySection")
    void securitySectionUpdated(final TestCase<List<ChangesFileSection>> test) {
        final Builder builder = ChangesFile.builder();
        if (test.currentText != null) {
            builder.sections(test.currentText);
        }
        final List<ChangesFileSection> updatedSections = getUpdatedChanges(test.vulnerabilities, builder.build())
                .getSections();
        for (int i = 0; i < test.expected.size(); i++) {
            assertThat("Section #" + i + " '" + test.expected.get(i).getHeading() + "'", updatedSections.get(i),
                    equalTo(test.expected.get(i)));
        }
        assertThat(updatedSections, hasSize(test.expected.size()));
        assertThat(updatedSections, equalTo(test.expected));
    }

    @ParameterizedTest
    @ValueSource(strings = { "* ISSUE_NUMBER: description", " * ISSUE_NUMBER: description",
            "* ISSUE_NUMBER: description ", "\t* ISSUE_NUMBER: description", "* ISSUE_NUMBER: description\n" })
    void defaultFeatureSectionRemoved(final String fixedIssuesTemplate) {
        final Builder builder = ChangesFile.builder();
        builder.addSection(
                ChangesFileSection.builder("## Features").addLine("").addLine(fixedIssuesTemplate).addLine("").build());
        final List<ChangesFileSection> updatedSections = getUpdatedChanges(List.of(vulnerability(1)), builder.build())
                .getSections();
        final Optional<ChangesFileSection> featuresSection = updatedSections.stream()
                .filter(section -> section.getHeading().equals("## Features")).findAny();
        assertThat("Features section was removed", featuresSection.isEmpty(), is(true));
    }

    @Test
    void nonDefaultFeatureSectionNotRemoved() {
        final Builder builder = ChangesFile.builder();
        final ChangesFileSection originalFeaturesSection = ChangesFileSection.builder("## Features").addLine("")
                .addLine("* #1: Fixed a bug").addLine("* ISSUE_NUMBER: description").addLine("").build();
        builder.addSection(originalFeaturesSection);
        final List<ChangesFileSection> updatedSections = getUpdatedChanges(List.of(vulnerability(1)), builder.build())
                .getSections();
        final Optional<ChangesFileSection> featuresSection = updatedSections.stream()
                .filter(section -> section.getHeading().equals("## Features")).findAny();
        assertThat(featuresSection.get(), equalTo(originalFeaturesSection));
    }

    private static ChangesFileSection securitySection(final String... lines) {
        return section("## Security", lines);
    }

    private static ChangesFileSection section(final String heading, final String... lines) {
        return ChangesFileSection.builder(heading).addLines(lines).build();
    }

    private static Vulnerability vulnerability(final int i) {
        return vulnerability(i, null);
    }

    private static Vulnerability vulnerability(final int i, final List<String> references) {
        return new Vulnerability("cve-" + i, "cwe-" + i, "description-" + i, "coordinates-" + i, references,
                "https://github.com/exasol/project/issues/" + i);
    }

    private ChangesFile getUpdatedChanges(final List<Vulnerability> vulnerabilities,
            final ChangesFile inputChangesFile) {
        when(vulnerabilityInfoProviderMock.getVulnerabilities()).thenReturn(vulnerabilities);
        if (!vulnerabilities.isEmpty()) {
            when(changesFileIOMock.read(CHANGES_FILE)).thenReturn(inputChangesFile);
        }
        testee().updateChanges(VERSION);

        if (vulnerabilities.isEmpty()) {
            verifyNoInteractions(changesFileIOMock);
            return null;
        } else {
            final ChangesFile updatedChangesFile = verifyChangesFileWritten();
            assertAll(() -> assertThat(updatedChangesFile, not(nullValue())),
                    () -> assertThat(updatedChangesFile, not(sameInstance(inputChangesFile))));
            return updatedChangesFile;
        }
    }

    private ChangesFile verifyChangesFileWritten() {
        final ArgumentCaptor<ChangesFile> arg = ArgumentCaptor.forClass(ChangesFile.class);
        verify(changesFileIOMock).write(arg.capture(), eq(CHANGES_FILE));
        return arg.getValue();
    }

    private ChangesFileUpdater testee() {
        return new ChangesFileUpdater(vulnerabilityInfoProviderMock, changesFileIOMock, PROJECT_DIR);
    }

    private static Arguments testCaseSecurity(final List<ChangesFileSection> currentLines,
            final List<Vulnerability> vulnerabilities, final List<ChangesFileSection> expectedLines) {
        return Arguments.of(new TestCase<List<ChangesFileSection>>(currentLines, vulnerabilities, expectedLines));
    }

    private static Arguments testCase(final List<String> currentLines, final List<Vulnerability> vulnerabilities,
            final List<String> expectedLines) {
        return Arguments.of(new TestCase<List<String>>(currentLines, vulnerabilities, expectedLines));
    }

    private static Arguments testCase(final String currentText, final List<Vulnerability> vulnerabilities,
            final String expectedText) {
        return Arguments.of(new TestCase<String>(currentText, vulnerabilities, expectedText));
    }

    private static record TestCase<T>(T currentText, List<Vulnerability> vulnerabilities, T expected) {
    }
}
