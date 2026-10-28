package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.ExasolVersionMatcher;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.AbstractFileValidator;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding.Fix;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangesFileValidator extends AbstractFileValidator {
    private static final int MAX_GITHUB_RELEASE_TITLE_LENGTH = 256;
    /** Section heading for the "Features" section used in the template. */
    public static final String FEATURES_SECTION = "## Features";
    /** Template for a fixed issue in the changes file. */
    public static final String FIXED_ISSUE_TEMPLATE = "* ISSUE_NUMBER: description";
    private final String projectName;
    private final List<AnalyzedSource> sources;
    private final String projectVersion;
    private final ChangesFileIO changesFileIO;

    /**
     * Create a new instance of {@link ChangesFileValidator}
     *
     * @param projectVersion   version of the project to validate
     * @param projectName      name of the maven project
     * @param projectDirectory root directory of the maven project
     * @param sources          source projects
     */
    public ChangesFileValidator(final String projectVersion, final String projectName, final Path projectDirectory,
            final List<AnalyzedSource> sources) {
        this(projectVersion, projectName, projectDirectory, sources, new ChangesFileIO());
    }

    ChangesFileValidator(final String projectVersion, final String projectName, final Path projectDirectory,
            final List<AnalyzedSource> sources, final ChangesFileIO changesFileIO) {
        super(projectDirectory, ChangesFile.getPathForVersion(projectVersion));
        this.projectVersion = projectVersion;
        this.projectName = projectName;
        this.sources = sources;
        this.changesFileIO = changesFileIO;
    }

    @Override
    protected void writeTemplateFile(final Path target) {
        changesFileIO.write(getTemplate(), target);
    }

    @Override
    protected boolean isValidationEnabled() {
        return !new ExasolVersionMatcher().isSnapshotVersion(this.projectVersion);
    }

    // [impl->dsn~verify-release-mode.verify-changes-file~1]
    @Override
    protected List<ValidationFinding> validateContent(final Path file) {
        final ChangesFile changesFile = changesFileIO.read(file);
        return Stream
                .of(validateDependencySection(file, changesFile),
                        validateSummarySection(changesFile),
                        validateProjectName(changesFile),
                        validateReleaseTitleLength(changesFile))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    // [impl->dsn~verify-release-mode.release-title-length~1]
    private Optional<ValidationFinding> validateReleaseTitleLength(final ChangesFile changesFile) {
        final String codeName = changesFile.getCodeName();
        if (codeName == null) {
            return noFinding();
        }
        final int titleLength = changesFile.getReleaseTitle(this.projectVersion).length();
        if (titleLength <= MAX_GITHUB_RELEASE_TITLE_LENGTH) {
            return noFinding();
        }
        return finding(ExaError.messageBuilder("E-PK-CORE-217")
                .message("GitHub release title in {{path}} has {{actual length}} characters; the maximum is {{maximum length}}.",
                        relativeFilePath, titleLength, MAX_GITHUB_RELEASE_TITLE_LENGTH)
                .mitigation("Shorten the code name.").toString());
    }

    private Optional<ValidationFinding> validateDependencySection(final Path file, final ChangesFile changesFile) {
        final ChangesFile fixed = fixDependencyUpdateSection(changesFile);
        if (changesFile.equals(fixed)) {
            return noFinding();
        } else {
            return outdatedDependencySectionFinding(fixed, file);
        }
    }

    private Optional<ValidationFinding> outdatedDependencySectionFinding(final ChangesFile fixedSections,
            final Path file) {
        final String errorMessage = ExaError.messageBuilder("E-PK-CORE-40")
                .message("Changes file is invalid.\nExpected content:\n{{expected content}}")
                .parameter("expected content", fixedSections.toString()).toString();
        final Fix fix = (final Logger log) -> changesFileIO.write(fixedSections, file);
        return Optional.of(SimpleValidationFinding.withMessage(errorMessage).andFix(fix).build());
    }

    private Optional<ValidationFinding> validateSummarySection(final ChangesFile changesFile) {
        final Optional<ChangesFileSection> summary = changesFile.getSummarySection();
        if (summary.isEmpty()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-193")
                    .message("Section '## Summary' is missing in {{path}}.", relativeFilePath)
                    .mitigation("Add section.").toString());
        } else {
            return noFinding();
        }
    }

    private Optional<ValidationFinding> validateProjectName(final ChangesFile changesFile) {
        if (changesFile.getProjectName() == null || changesFile.getProjectName().isBlank()) {
            return finding(ExaError.messageBuilder("E-PK-CORE-195")
                    .message("Project name in {{path}} is missing.", relativeFilePath).mitigation("Add a project name.")
                    .toString());
        } else {
            return noFinding();
        }
    }

    private Optional<ValidationFinding> finding(final String message) {
        return Optional.of(SimpleValidationFinding.withMessage(message).build());
    }

    private Optional<ValidationFinding> noFinding() {
        return Optional.empty();
    }

    private ChangesFile fixDependencyUpdateSection(final ChangesFile changesFile) {
        final var dependencySectionFixer = new DependencySectionFixer(this.sources);
        return dependencySectionFixer.fix(changesFile);
    }

    private ChangesFile getTemplate() {
        final String releaseDate = LocalDateTime.now().getYear() + "-??-??";
        final var changesFile = ChangesFile.builder().projectName(this.projectName).projectVersion(this.projectVersion)
                .releaseDate(releaseDate) //
                .codeName("") //
                .summary(ChangesFileSection.builder("## Summary").build())
                .addSection(ChangesFileSection.builder(FEATURES_SECTION).addLines("", FIXED_ISSUE_TEMPLATE, "").build()) //
                .build();
        return fixDependencyUpdateSection(changesFile);
    }
}
