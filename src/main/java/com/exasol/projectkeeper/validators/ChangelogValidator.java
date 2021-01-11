package com.exasol.projectkeeper.validators;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * Validator that checks the existence of the doc/changes/changes_X.X.X.md file for the current project's version.
 */
public class ChangelogValidator implements Validator {
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private final MavenProject mavenProject;

    /**
     * Create a new instance of {@link ChangelogValidator}
     * 
     * @param mavenProject maven project
     */
    public ChangelogValidator(final MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    @Override
    public ChangelogValidator validate(final Consumer<ValidationFinding> findingConsumer) {
        final String version = this.mavenProject.getVersion();
        if (!isSnapshotVersion(version)) {
            validateThatChangesFileExists(findingConsumer, version);
        }
        return this;
    }

    private boolean isSnapshotVersion(final String version) {
        return version.endsWith(SNAPSHOT_SUFFIX);
    }

    private void validateThatChangesFileExists(final Consumer<ValidationFinding> findingConsumer,
            final String version) {
        final File changesFile = this.mavenProject.getBasedir().toPath()
                .resolve(Path.of("doc", "changes", "changes_" + version + ".md")).toFile();
        if (!changesFile.exists()) {
            final String changesFileRelativeToProjectRoot = this.mavenProject.getBasedir().toPath()
                    .relativize(changesFile.toPath()).toString();
            findingConsumer
                    .accept(new ValidationFinding("E-PK-20 Could not find '" + changesFileRelativeToProjectRoot + "'.",
                            getFix(changesFile, changesFileRelativeToProjectRoot)));
        } else {
            checkFileIsDifferentToTemplate(findingConsumer, changesFile);
        }
    }

    private void checkFileIsDifferentToTemplate(final Consumer<ValidationFinding> findingConsumer,
            final File changesFile) {
        try {
            if (Files.readString(changesFile.toPath()).equals(getTemplate())) {
                findingConsumer.accept(
                        new ValidationFinding("E-PK-22 Please change the content of '" + changesFile + "' by hand!"));
            }
        } catch (final IOException exception) {
            throw new IllegalStateException("E-PK-23 Failed to open '" + changesFile + "' for read.", exception);
        }
    }

    private ValidationFinding.Fix getFix(final File changesFile, final String changesFileRelativeToProjectRoot) {
        return (Log log) -> {
            try {
                changesFile.toPath().getParent().toFile().mkdirs();
                try (final FileWriter fileWriter = new FileWriter(changesFile)) {
                    fileWriter.write(getTemplate());
                }
                log.warn("Created '" + changesFileRelativeToProjectRoot + "'. Don't forget to update it's content!");
            } catch (final IOException exception) {
                throw new IllegalStateException("E-PK-21 Failed to create '" + changesFileRelativeToProjectRoot + "'. ",
                        exception);
            }
        };
    }

    private String getTemplate() {
        final StringBuilder templateBuilder = new StringBuilder();
        templateBuilder.append("#");
        templateBuilder.append(this.mavenProject.getName());
        templateBuilder.append(" ");
        templateBuilder.append(this.mavenProject.getVersion());
        templateBuilder.append(", released ");
        templateBuilder.append(LocalDateTime.now().getYear());
        templateBuilder.append("-??-??");
        templateBuilder.append("\n\n");
        templateBuilder.append("## Code name: \n\n");
        templateBuilder.append("## Features / Enhancements\n\n* ISSUE_NUMBER: description\n");
        return templateBuilder.toString();
    }
}
