package com.exasol.projectkeeper.validators.release;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.stream.Stream;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.changesfile.*;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;
import com.exasol.projectkeeper.validators.release.github.GitHubAdapter;
import com.exasol.projectkeeper.validators.release.github.IssueState;

class ChangesFileReleaseValidator implements Validator {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private final ChangesFile changesFile;
    private final Path changesFilePath;
    private final Clock clock;
    private final GitHubAdapter gitHubAdapter;

    ChangesFileReleaseValidator(final String repoName, final Path changesFilePath, final ChangesFile changesFile) {
        this(changesFilePath, changesFile, GitHubAdapter.connect(repoName), Clock.systemUTC());
    }

    ChangesFileReleaseValidator(final Path changesFilePath, final ChangesFile changesFile,
            final GitHubAdapter gitHubAdapter, final Clock clock) {
        this.changesFilePath = changesFilePath;
        this.changesFile = changesFile;
        this.gitHubAdapter = gitHubAdapter;
        this.clock = clock;
    }

    // [impl->dsn~verify-release-mode.verify-changes-file~1]
    @Override
    public List<ValidationFinding> validate() {
        return Stream.of(validateReleaseDate(), validateIssuesClosed(), validateSummary(), validateCodeName()) //
                .filter(Optional::isPresent) //
                .map(Optional::get) //
                .toList();
    }

    private Optional<ValidationFinding> validateSummary() {
        final Optional<ChangesFileSection> summarySection = changesFile.getSummarySection();
        if (summarySection.isEmpty()) {
            return noFindings(); // Already checked in ChangesFileValidator
        }
        if (summarySection.get().getContent().stream().filter(not(String::isBlank)).findAny().isEmpty()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-194")
                    .message("Section '## Summary' is empty in {{path}}.", changesFilePath)
                    .mitigation("Add content to section.").toString());
        }
        return noFindings();
    }

    private Optional<ValidationFinding> validateCodeName() {
        if (changesFile.getCodeName() == null || changesFile.getCodeName().isBlank()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-197")
                    .message("Code name in {{path}} is missing.", changesFilePath).mitigation("Add a code name.")
                    .toString());
        }
        return noFindings();
    }

    // [impl->dsn~verify-release-mode.verify-release-date~1]
    private Optional<ValidationFinding> validateReleaseDate() {
        final Optional<LocalDate> releaseDate = changesFile.getParsedReleaseDate();
        if (releaseDate.isEmpty()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-182")
                    .message("Release date {{release date}} has invalid format in {{changes file path}}",
                            changesFile.getReleaseDate(), changesFilePath)
                    .toString());
        }
        final LocalDate today = today();
        if (!releaseDate.get().equals(today)) {
            return finding(ExaError.messageBuilder("E-PK-CORE-183")
                    .message("Release date {{actual date}} must be today {{today}} in {{changes file path}}",
                            releaseDate.get(), today, changesFilePath)
                    .toString());
        }
        return noFindings();
    }

    // [impl->dsn~verify-release-mode.verify-issues-closed~1]
    private Optional<ValidationFinding> validateIssuesClosed() {
        final List<Integer> wrongIssues = getIssuesWronglyMarkedAsClosed();
        if (!wrongIssues.isEmpty()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-186").message(
                    "The following GitHub issues are marked as fixed in {{changes file}} but are not closed in GitHub: {{issue numbers}}",
                    changesFilePath, wrongIssues).toString());
        }
        return noFindings();
    }

    private List<Integer> getIssuesWronglyMarkedAsClosed() {
        final Set<Integer> mentionedTickets = changesFile.getFixedIssues().stream().map(FixedIssue::issueNumber)
                .collect(toSet());
        final Set<Integer> stillOpenIssues = new HashSet<>();
        for (final Integer issue : mentionedTickets) {
            final IssueState state = gitHubAdapter.getIssueState(issue);
            if (state != IssueState.CLOSED) {
                stillOpenIssues.add(issue);
            }
        }
        return sort(stillOpenIssues);
    }

    private List<Integer> sort(final Set<Integer> numbers) {
        final ArrayList<Integer> list = new ArrayList<>(numbers);
        list.sort(Comparator.naturalOrder());
        return list;
    }

    private LocalDate today() {
        return LocalDate.ofInstant(clock.instant(), UTC_ZONE);
    }

    private Optional<ValidationFinding> noFindings() {
        return Optional.empty();
    }

    private Optional<ValidationFinding> finding(final String message) {
        return Optional.of(SimpleValidationFinding.withMessage(message).build());
    }
}
