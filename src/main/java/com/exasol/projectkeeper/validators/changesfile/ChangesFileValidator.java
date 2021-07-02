package com.exasol.projectkeeper.validators.changesfile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.pom.MavenProjectFromFileReader;
import com.exasol.projectkeeper.validators.AbstractFileValidator;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangesFileValidator extends AbstractFileValidator {
    private final String projectName;
    private final Path projectDirectory;
    private final String projectVersion;
    private final MavenProjectFromFileReader mavenModelReader;

    /**
     * Create a new instance of {@link ChangesFileValidator}
     * 
     * @param projectVersion   version of the project to validate
     * @param projectName      name of the maven project
     * @param projectDirectory root directory of the maven project
     * @param mavenModelReader reader for maven models
     */
    public ChangesFileValidator(final String projectVersion, final String projectName, final Path projectDirectory,
            final MavenProjectFromFileReader mavenModelReader) {
        super(projectDirectory, Path.of("doc", "changes", "changes_" + projectVersion + ".md"));
        this.projectVersion = projectVersion;
        this.projectName = projectName;
        this.projectDirectory = projectDirectory;
        this.mavenModelReader = mavenModelReader;
    }

    @Override
    protected void writeTemplateFile(final Path target) throws IOException {
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
        return ValidationFinding
                .withMessage(ExaError.messageBuilder("E-PK-40")
                        .message("Changes file is invalid.\nExpected content:\n{{expected content}}")
                        .parameter("expected content", fixedSections.toString()).toString())
                .andFix((log -> new ChangesFileIO().write(fixedSections, file))).build();
    }

    private ChangesFile fixSections(final ChangesFile changesFile) {
        final var dependencySectionFixer = new DependencySectionFixer(this.mavenModelReader, this.projectDirectory);
        return dependencySectionFixer.fix(changesFile);
    }

    private ChangesFile getTemplate() {
        final var changesFile = ChangesFile.builder()
                .setHeader(List.of("# " + this.projectName + " " + this.projectVersion + ", released "
                        + LocalDateTime.now().getYear() + "-??-??", "", "Code name:", ""))//
                .addSection(List.of("## Features", "", "* ISSUE_NUMBER: description"))//
                .build();
        return fixSections(changesFile);
    }
}
