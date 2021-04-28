package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.pom.MavenFileModelReader;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangesFileValidator implements Validator {
    private final Path relativePathToChangesFile;
    private final String projectName;
    private final Path changesFileAbsolutePath;
    private final Path projectDirectory;
    private final String projectVersion;
    private final MavenFileModelReader mavenModelReader;

    /**
     * Create a new instance of {@link ChangesFileValidator}
     * 
     * @param projectVersion   version of the project to validate
     * @param projectName      name of the maven project
     * @param projectDirectory root directory of the maven project
     * @param mavenModelReader reader for maven models
     */
    public ChangesFileValidator(final String projectVersion, final String projectName, final Path projectDirectory,
            final MavenFileModelReader mavenModelReader) {
        this.projectVersion = projectVersion;
        this.relativePathToChangesFile = Path.of("doc", "changes", "changes_" + projectVersion + ".md");
        this.projectName = projectName;
        this.changesFileAbsolutePath = projectDirectory.resolve(this.relativePathToChangesFile);
        this.projectDirectory = projectDirectory;
        this.mavenModelReader = mavenModelReader;
    }

    @Override
    public List<ValidationFinding> validate() {
        if (!new ExasolVersionMatcher().isSnapshotVersion(this.projectVersion)) {
            return runValidation();
        } else {
            return Collections.emptyList();
        }
    }

    private List<ValidationFinding> runValidation() {
        if (!this.changesFileAbsolutePath.toFile().exists()) {
            return List.of(getMissingChangesFileFinding());
        } else {
            return validateContent();
        }
    }

    private List<ValidationFinding> validateContent() {
        final var changesFile = new ChangesFileIO().read(this.changesFileAbsolutePath);
        final var fixedSections = fixSections(changesFile);
        if (!changesFile.equals(fixedSections)) {
            return List.of(getWrongContentFinding(fixedSections));
        } else {
            return Collections.emptyList();
        }
    }

    private ValidationFinding getWrongContentFinding(final ChangesFile fixedSections) {
        return new ValidationFinding(
                ExaError.messageBuilder("E-PK-40")
                        .message("Changes file is invalid.\nExpected content:\n{{expected content}}")
                        .parameter("expected content", fixedSections.toString()).toString(),
                (log -> new ChangesFileIO().write(fixedSections, this.changesFileAbsolutePath)));
    }

    private ValidationFinding getMissingChangesFileFinding() {
        return new ValidationFinding(
                ExaError.messageBuilder("E-PK-20").message("Could not find {{changes file}}.")
                        .parameter("changes file", this.relativePathToChangesFile.toString()).toString(),
                getCreateFileFix(this.changesFileAbsolutePath));
    }

    private ChangesFile fixSections(final ChangesFile changesFile) {
        final var dependencySectionFixer = new DependencySectionFixer(this.mavenModelReader, this.projectDirectory);
        return dependencySectionFixer.fix(changesFile);
    }

    private ValidationFinding.Fix getCreateFileFix(final Path changesFile) {
        return (Log log) -> {
            changesFile.getParent().toFile().mkdirs();
            new ChangesFileIO().write(getTemplate(), changesFile);
            log.warn("Created '" + this.relativePathToChangesFile + "'. Don't forget to update it's content!");
        };
    }

    private ChangesFile getTemplate() {
        final var changesFile = ChangesFile.builder()
                .setHeader(List.of("# " + this.projectName + " " + this.projectVersion + ", released "
                        + LocalDateTime.now().getYear() + "-??-??", ""))//
                .addSection(List.of("## Features", "", "* ISSUE_NUMBER: description"))//
                .build();
        return fixSections(changesFile);
    }
}
