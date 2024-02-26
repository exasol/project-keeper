package com.exasol.projectkeeper.dependencyupdate;

import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.changesfile.*;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileSection.Builder;

/**
 * This class updates the the changesfile (e.g. {@code doc/changes/changes_1.2.0.md} for a given version, adding
 * information about fixed vulnerabilities in dependencies.
 */
// [impl->dsn~dependency-updater.update-changelog~1]
class ChangesFileUpdater {
    private static final Pattern ISSUE_URL_PATTERN = Pattern.compile("^https://github.com/exasol/[^/]+/issues/(\\d+)$");
    private static final String SECURITY_SECTION_HEADER = "## Security";
    private final ChangesFileIO changesFileIO;
    private final Path projectDir;
    private final VulnerabilityInfoProvider vulnerabilityInfoProvider;

    ChangesFileUpdater(final VulnerabilityInfoProvider vulnerabilityInfoProvider, final ChangesFileIO changesFileIO,
            final Path projectDir) {
        this.vulnerabilityInfoProvider = vulnerabilityInfoProvider;
        this.changesFileIO = changesFileIO;
        this.projectDir = projectDir;
    }

    void updateChanges(final String version) {
        final List<Vulnerability> vulnerabilities = vulnerabilityInfoProvider.getVulnerabilities();
        if (vulnerabilities.isEmpty()) {
            return;
        }
        final Path changesFilePath = getChangesFilePath(version);
        final ChangesFile changesFile = changesFileIO.read(changesFilePath);
        final ChangesFile updatedChanges = new Updater(changesFile, vulnerabilities).update();
        changesFileIO.write(updatedChanges, changesFilePath);
    }

    private Path getChangesFilePath(final String version) {
        return projectDir.resolve(ChangesFile.getPathForVersion(version));
    }

    private static class Updater {
        private final ChangesFile changesFile;
        private final List<Vulnerability> vulnerabilities;

        private Updater(final ChangesFile changesFile, final List<Vulnerability> vulnerabilities) {
            this.changesFile = changesFile;
            this.vulnerabilities = vulnerabilities;
        }

        private ChangesFile update() {
            return changesFile.toBuilder() //
                    .codeName(append(changesFile.getCodeName(), ", ", createCodeName())) //
                    .summary(createSummary()) //
                    .sections(createOtherSections()) //
                    .build();
        }

        private String createCodeName() {
            if (vulnerabilities.size() == 1) {
                final Vulnerability vulnerability = vulnerabilities.get(0);
                return "Fixed vulnerability " + vulnerability.cve() + " in " + vulnerability.coordinates();
            }
            return "Fixed vulnerabilities " + vulnerabilities.stream().map(Vulnerability::cve).collect(joining(", "));
        }

        private String append(final String currentText, final String separator, final String newText) {
            return Optional.ofNullable(currentText) //
                    .filter(Predicate.not(String::isBlank)) //
                    .map(text -> text + separator + newText) //
                    .orElse(newText);
        }

        private ChangesFileSection createSummary() {
            return changesFile.getSummarySection() //
                    .map(ChangesFileSection::toBuilder) //
                    .orElseGet(() -> ChangesFileSection.builder("## Summary")) //
                    .addLine("") //
                    .addLine("This release fixes the following " + (vulnerabilities.size() == 1 ? "vulnerability"
                            : vulnerabilities.size() + " vulnerabilities") + ":") //
                    .addLine("")
                    .addLines(vulnerabilities.stream().map(this::renderVulnerability).flatMap(List::stream).toList()) //
                    .build();
        }

        private List<String> renderVulnerability(final Vulnerability vulnerability) {
            final List<String> lines = new ArrayList<>();
            lines.add("### " + vulnerability.cve() + " (" + vulnerability.cwe() + ") in dependency `"
                    + vulnerability.coordinates() + "`");
            lines.add(vulnerability.description());
            if (vulnerability.references() != null && !vulnerability.references().isEmpty()) {
                lines.add("#### References");
                lines.addAll(vulnerability.references().stream().map(link -> "* " + link).toList());
            }
            lines.add("");
            return lines;
        }

        private List<ChangesFileSection> createOtherSections() {
            final List<ChangesFileSection> sections = new ArrayList<>();
            sections.add(buildSecuritySection());
            sections.addAll(changesFile.getSections().stream()
                    .filter(section -> !section.getHeading().equals(SECURITY_SECTION_HEADER)).toList());
            return sections;
        }

        private ChangesFileSection buildSecuritySection() {
            final Builder securitySectionBuilder = getExistingSecuritySection();
            securitySectionBuilder.addLines(vulnerabilities.stream().map(this::createIssueFixesEntry).toList());
            securitySectionBuilder.addLine("");
            return securitySectionBuilder.build();
        }

        private Builder getExistingSecuritySection() {
            return this.changesFile.getSections().stream() //
                    .filter(section -> section.getHeading().equals(SECURITY_SECTION_HEADER)) //
                    .map(ChangesFileSection::toBuilder) //
                    .findFirst() //
                    .orElseGet(() -> ChangesFileSection.builder(SECURITY_SECTION_HEADER).addLine(""));
        }

        private String createIssueFixesEntry(final Vulnerability vulnerability) {
            return "* #" + getIssueNumber(vulnerability.issueUrl()) + ": Fixed vulnerability " + vulnerability.cve()
                    + " in dependency `" + vulnerability.coordinates() + "`";
        }

        private String getIssueNumber(final String issueUrl) {
            final Matcher matcher = ISSUE_URL_PATTERN.matcher(issueUrl);
            if (!matcher.matches()) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-181")
                        .message("Issues URL {{url}} does not match expected pattern {{pattern}}.", issueUrl,
                                ISSUE_URL_PATTERN)
                        .ticketMitigation().toString());
            }
            return matcher.group(1);
        }
    }
}
