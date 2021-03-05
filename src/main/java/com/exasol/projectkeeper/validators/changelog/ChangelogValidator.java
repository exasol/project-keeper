package com.exasol.projectkeeper.validators.changelog;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangelogValidator implements Validator {
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private final MavenProject mavenProject;
    private final Path relativePathToChangesFile;
    private final Path changesFileAbsolutePath;

    /**
     * Create a new instance of {@link ChangelogValidator}
     * 
     * @param mavenProject maven project
     */
    public ChangelogValidator(final MavenProject mavenProject) {
        this.mavenProject = mavenProject;
        this.relativePathToChangesFile = Path.of("doc", "changes", "changes_" + mavenProject.getVersion() + ".md");
        this.changesFileAbsolutePath = this.mavenProject.getBasedir().toPath().resolve(this.relativePathToChangesFile);
    }

    @Override
    public ChangelogValidator validate(final Consumer<ValidationFinding> findingConsumer) {
        if (!isSnapshotVersion(this.mavenProject.getVersion())) {
            runValidation(findingConsumer);
        }
        return this;
    }

    private void runValidation(final Consumer<ValidationFinding> findingConsumer) {
        if (validateThatChangesFileExists(findingConsumer)) {
            final ChangelogFile changelogFile = new ChangelogIO().read(this.changesFileAbsolutePath);
            final ChangelogFile fixedChangelog = fixChangelogSections(changelogFile);
            if (!changelogFile.equals(fixedChangelog)) {
                findingConsumer.accept(new ValidationFinding(
                        ExaError.messageBuilder("E-PK-40").message("Changes file is invalid.").toString(),
                        (log -> new ChangelogIO().write(fixedChangelog, this.changesFileAbsolutePath))));
            }
        }
    }

    private ChangelogFile fixChangelogSections(final ChangelogFile changelogFile) {
        final DependencySectionFixer dependencySectionFixer = new DependencySectionFixer(
                this.mavenProject.getBasedir().toPath());
        return dependencySectionFixer.fix(changelogFile);
    }

    private boolean isSnapshotVersion(final String version) {
        return version.endsWith(SNAPSHOT_SUFFIX);
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
            new ChangelogIO().write(getTemplate(), changesFile);
            log.warn("Created '" + this.relativePathToChangesFile + "'. Don't forget to update it's content!");
        };
    }

    private ChangelogFile getTemplate() {
        final ChangelogFile changelogFile = ChangelogFile.builder()
                .setHeader(List.of("# " + this.mavenProject.getName() + " " + this.mavenProject.getVersion()
                        + ", released " + LocalDateTime.now().getYear() + "-??-??", ""))//
                .addSection(List.of("## Features", "", "* ISSUE_NUMBER: description"))//
                .build();
        return fixChangelogSections(changelogFile);
    }
}
