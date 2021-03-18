package com.exasol.projectkeeper.validators.changesfile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.MavenModelReader;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangesFileValidator implements Validator {
    private final Path relativePathToChangesFile;
    private final String projectName;
    private final Path changesFileAbsolutePath;
    private final Path projectDirectory;
    private final String projectVersion;
    private final MavenModelReader mavenModelReader;

    /**
     * Create a new instance of {@link ChangesFileValidator}
     * 
     * @param projectVersion   version of the project to validate
     * @param projectName      name of the maven project
     * @param projectDirectory root directory of the maven project
     * @param mavenModelReader reader for maven models
     */
    public ChangesFileValidator(final String projectVersion, final String projectName, final Path projectDirectory,
            final MavenModelReader mavenModelReader) {
        this.projectVersion = projectVersion;
        this.relativePathToChangesFile = Path.of("doc", "changes", "changes_" + projectVersion + ".md");
        this.projectName = projectName;
        this.changesFileAbsolutePath = projectDirectory.resolve(this.relativePathToChangesFile);
        this.projectDirectory = projectDirectory;
        this.mavenModelReader = mavenModelReader;
    }

    @Override
    public ChangesFileValidator validate(final Consumer<ValidationFinding> findingConsumer) {
        if (!new ExasolVersionMatcher().isSnapshotVersion(this.projectVersion)) {
            runValidation(findingConsumer);
        }
        return this;
    }

    private void runValidation(final Consumer<ValidationFinding> findingConsumer) {
        if (validateThatChangesFileExists(findingConsumer)) {
            final ChangesFile changesFile = new ChangesFileIO().read(this.changesFileAbsolutePath);
            final ChangesFile fixedSections = fixSections(changesFile);
            if (!changesFile.equals(fixedSections)) {
                findingConsumer.accept(new ValidationFinding(
                        ExaError.messageBuilder("E-PK-40")
                                .message("Changes file is invalid.\nExpected content:\n{{expected content}}")
                                .parameter("expected content", fixedSections.toString()).toString(),
                        (log -> new ChangesFileIO().write(fixedSections, this.changesFileAbsolutePath))));
            }
        }
    }

    private ChangesFile fixSections(final ChangesFile changesFile) {
        final DependencySectionFixer dependencySectionFixer = new DependencySectionFixer(this.mavenModelReader,
                this.projectDirectory);
        return dependencySectionFixer.fix(changesFile);
    }

    private boolean validateThatChangesFileExists(final Consumer<ValidationFinding> findingConsumer) {
        if (!this.changesFileAbsolutePath.toFile().exists()) {
            findingConsumer.accept(new ValidationFinding(
                    ExaError.messageBuilder("E-PK-20").message("Could not find {{changes file}}.")
                            .parameter("changes file", this.relativePathToChangesFile.toString()).toString(),
                    getCreateFileFix(this.changesFileAbsolutePath)));
            return false;
        } else {
            return true;
        }
    }

    private ValidationFinding.Fix getCreateFileFix(final Path changesFile) {
        return (Log log) -> {
            changesFile.getParent().toFile().mkdirs();
            new ChangesFileIO().write(getTemplate(), changesFile);
            log.warn("Created '" + this.relativePathToChangesFile + "'. Don't forget to update it's content!");
        };
    }

    private ChangesFile getTemplate() {
        final ChangesFile changesFile = ChangesFile.builder()
                .setHeader(List.of("# " + this.projectName + " " + this.projectVersion + ", released "
                        + LocalDateTime.now().getYear() + "-??-??", ""))//
                .addSection(List.of("## Features", "", "* ISSUE_NUMBER: description"))//
                .build();
        return fixSections(changesFile);
    }
}
