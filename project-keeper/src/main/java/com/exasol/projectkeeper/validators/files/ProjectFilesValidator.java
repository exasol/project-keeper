package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXACT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the projects file structure.
 */
//[impl->dsn~required-files-validator~1]
public class ProjectFilesValidator implements Validator {
    private final Collection<ProjectKeeperModule> enabledModules;
    private final Path projectDirectory;

    /**
     * Crate a new instance of {@link ProjectFilesValidator}.
     * 
     * @param enabledModules   list of enabled {@link ProjectKeeperModule}s
     * @param projectDirectory project's root directory
     */
    public ProjectFilesValidator(final Collection<ProjectKeeperModule> enabledModules, final Path projectDirectory) {
        this.enabledModules = enabledModules;
        this.projectDirectory = projectDirectory;
    }

    @Override
    public List<ValidationFinding> validate() {
        return FileTemplates.FILE_TEMPLATES.stream()
                .filter(template -> this.enabledModules.contains(template.getModule()))
                .flatMap(template -> validate(template).stream())//
                .collect(Collectors.toList());
    }

    private List<ValidationFinding> validate(final FileTemplate template) {
        final Path projectFile = this.projectDirectory.resolve(template.getPathInProject());
        final String templateContent = template.getContent();
        if (!Files.exists(projectFile)) {
            return List.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-17").message("Missing required: {{required file}}")
                            .parameter("required file", template.getPathInProject().toString()).toString())
                    .andFix((Logger log) -> fixFile(projectFile, templateContent)).build());
        }
        if (template.getTemplateType().equals(REQUIRE_EXACT)) {
            return validateContent(template.getContent(), template.getPathInProject().toString(), projectFile);
        } else {
            return Collections.emptyList();
        }
    }

    private void fixFile(final Path projectFile, final String templateContent) {
        try {
            Files.createDirectories(projectFile.getParent());
            Files.writeString(projectFile, templateContent);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", projectFile).toString(),
                    exception);
        }
    }

    private List<ValidationFinding> validateContent(final String templateContent, final String templateName,
            final Path projectFile) {
        if (!isFileEqualWithTemplate(templateContent, projectFile)) {
            return List.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-18").message("Outdated content: {{file name}}")
                            .parameter("file name", templateName).toString())
                    .andFix((Logger log) -> fixFile(projectFile, templateContent)).build());
        } else {
            return Collections.emptyList();
        }
    }

    private boolean isFileEqualWithTemplate(final String templateContent, final Path projectFile) {
        try {
            final String actualContent = Files.readString(projectFile);
            return actualContent.equals(templateContent);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-19").message("Failed to validate {{file name}}'s content.")
                            .parameter("file name", projectFile).toString(),
                    exception);
        }
    }
}
