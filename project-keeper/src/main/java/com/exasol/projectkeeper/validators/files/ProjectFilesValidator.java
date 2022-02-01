package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXACT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

/**
 * Validator for the projects file structure.
 */
//[impl->dsn~required-files-validator~1]
public class ProjectFilesValidator implements Validator {
    private final Path projectDirectory;
    private final List<ProjectKeeperConfig.Source> sources;
    private final Logger logger;

    /**
     * Crate a new instance of {@link ProjectFilesValidator}.
     * 
     * @param projectDirectory project's root directory
     * @param sources          list of sources
     * @param logger           logger
     */
    public ProjectFilesValidator(final Path projectDirectory, final List<ProjectKeeperConfig.Source> sources,
            final Logger logger) {
        this.projectDirectory = projectDirectory;
        this.sources = sources;
        this.logger = logger;
    }

    @Override
    public List<ValidationFinding> validate() {
        final List<ValidationFinding> findings = new ArrayList<>();
        final FileTemplatesFactory templatesFactory = new FileTemplatesFactory(this.logger);
        findings.addAll(validateTemplatesRelativeToRepo(templatesFactory));
        findings.addAll(validateTemplatesRelativeToSource(templatesFactory));
        return findings;
    }

    private List<ValidationFinding> validateTemplatesRelativeToSource(final FileTemplatesFactory templatesFactory) {
        final List<ValidationFinding> findings = new ArrayList<>();
        for (final ProjectKeeperConfig.Source source : this.sources) {
            final Path sourceDir = directoryOf(source.getPath());
            final List<FileTemplate> templates = templatesFactory.getTemplatesForSource(source);
            findings.addAll(runValidation(templates, sourceDir));
        }
        return findings;
    }

    private List<ValidationFinding> validateTemplatesRelativeToRepo(final FileTemplatesFactory templatesFactory) {
        final List<FileTemplate> templates = templatesFactory.getGlobalTemplates(this.projectDirectory, this.sources);
        return runValidation(templates, this.projectDirectory);
    }

    private List<ValidationFinding> runValidation(final List<FileTemplate> templates, final Path relativeDirectory) {
        final List<ValidationFinding> findings = new ArrayList<>();
        for (final FileTemplate template : templates) {
            findings.addAll(validate(relativeDirectory, template));
        }
        return findings;
    }

    private Path directoryOf(final Path path) {
        if (Files.isDirectory(path)) {
            return path;
        } else {
            return path.getParent();
        }
    }

    private List<ValidationFinding> validate(final Path relativeDirectory, final FileTemplate template) {
        final Path projectFile = relativeDirectory.resolve(template.getPathInProject());
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
