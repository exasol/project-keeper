package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.ExasolVersionMatcher;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.AbstractFileValidator;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangesFileValidator extends AbstractFileValidator {
    private final String projectName;
    private final List<AnalyzedSource> sources;
    private final String projectVersion;

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
        super(projectDirectory, ChangesFile.getPathForVersion(projectVersion));
        this.projectVersion = projectVersion;
        this.projectName = projectName;
        this.sources = sources;
    }

    @Override
    protected void writeTemplateFile(final Path target) {
        new ChangesFileIO().write(getTemplate(), target);
    }

    @Override
    protected boolean isValidationEnabled() {
        return !new ExasolVersionMatcher().isSnapshotVersion(this.projectVersion);
    }

    @Override
    protected List<ValidationFinding> validateContent(final Path file) {
        final var changesFile = new ChangesFileIO().read(file);
        final var fixedSections = fixSections(changesFile);
        if (!changesFile.equals(fixedSections)) {
            return List.of(getWrongContentFinding(fixedSections, file));
        } else {
            return Collections.emptyList();
        }
    }

    private ValidationFinding getWrongContentFinding(final ChangesFile fixedSections, final Path file) {
        return SimpleValidationFinding
                .withMessage(ExaError.messageBuilder("E-PK-CORE-40")
                        .message("Changes file is invalid.\nExpected content:\n{{expected content}}")
                        .parameter("expected content", fixedSections.toString()).toString())
                .andFix(((final Logger log) -> new ChangesFileIO().write(fixedSections, file))).build();
    }

    private ChangesFile fixSections(final ChangesFile changesFile) {
        final var dependencySectionFixer = new DependencySectionFixer(this.sources);
        return dependencySectionFixer.fix(changesFile);
    }

    private ChangesFile getTemplate() {
        final String releaseDate = LocalDateTime.now().getYear() + "-??-??";
        final var changesFile = ChangesFile.builder().projectName(this.projectName).projectVersion(this.projectVersion)
                .releaseDate(releaseDate) //
                .codeName("") //
                .summary(ChangesFileSection.builder("## Summary").build())
                .addSection(ChangesFileSection.builder("## Features").addLines("", "* ISSUE_NUMBER: description", "")
                        .build()) //
                .build();
        return fixSections(changesFile);
    }
}
