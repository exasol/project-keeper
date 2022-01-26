package com.exasol.projectkeeper.validators.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;

/**
 * Validator for the projects file structure.
 */
//[impl->dsn~required-files-validator~1]
public class ProjectFilesValidator implements Validator {
    private final Collection<ProjectKeeperModule> enabledModules;
    private final File projectDirectory;

    /**
     * Crate a new instance of {@link ProjectFilesValidator}.
     * 
     * @param enabledModules   list of enabled {@link ProjectKeeperModule}s
     * @param projectDirectory project's root directory
     */
    public ProjectFilesValidator(final Collection<ProjectKeeperModule> enabledModules, final File projectDirectory) {
        this.enabledModules = enabledModules;
        this.projectDirectory = projectDirectory;
    }

    @Override
    public List<ValidationFinding> validate() {
        try (final var scanResult = new ClassGraph().acceptPaths("templates/").scan()) {
            return scanResult.getAllResources().stream()//
                    .map(FileTemplate::fromResource)//
                    .filter(template -> this.enabledModules.contains(template.module))
                    .flatMap(template -> validate(template).stream())//
                    .collect(Collectors.toList());
        }
    }

    private List<ValidationFinding> validate(final FileTemplate template) {
        final var projectFile = this.projectDirectory.toPath().resolve(template.fileName).toFile();
        final String templateContent = template.getContent();
        if (!projectFile.exists()) {
            return List.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-17").message("Missing required: {{required file}}")
                            .parameter("required file", template.fileName).toString())
                    .andFix((Logger log) -> fixFile(projectFile, templateContent)).build());
        }
        if (template.type.equals(TemplateType.REQUIRE_EXACT)) {
            return validateContent(template.getContent(), template.fileName, projectFile);
        } else {
            return Collections.emptyList();
        }
    }

    private void fixFile(final File projectFile, final String templateContent) {
        try {
            projectFile.getParentFile().mkdirs();
            Files.writeString(projectFile.toPath(), templateContent);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", projectFile.getAbsolutePath()).toString(),
                    exception);
        }
    }

    private List<ValidationFinding> validateContent(final String templateContent, final String templateName,
            final File projectFile) {
        if (!isFileEqualWithTemplate(templateContent, projectFile)) {
            return List.of(SimpleValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-CORE-18").message("Outdated content: {{file name}}")
                            .parameter("file name", templateName).toString())
                    .andFix((Logger log) -> fixFile(projectFile, templateContent)).build());
        } else {
            return Collections.emptyList();
        }
    }

    private boolean isFileEqualWithTemplate(final String templateContent, final File projectFile) {
        try {
            final String actualContent = Files.readString(projectFile.toPath());
            return actualContent.equals(templateContent);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-19").message("Failed to validate {{file name}}'s content.")
                            .parameter("file name", projectFile).toString(),
                    exception);
        }
    }

    enum TemplateType {
        REQUIRE_EXACT, REQUIRE_EXIST;

        private static TemplateType fromString(final String templateTypeString) {
            try {
                return TemplateType.valueOf(templateTypeString.toUpperCase());
            } catch (final IllegalArgumentException exception) {
                throw new IllegalArgumentException(
                        ExaError.messageBuilder("F-PK-CORE-3").message("Unknown template type {{type}}.")
                                .parameter("type", templateTypeString).ticketMitigation().toString());
            }
        }
    }

    private static class FileTemplate {
        private final Resource template;
        private final TemplateType type;
        private final String fileName;
        private final ProjectKeeperModule module;

        private FileTemplate(final Resource template, final TemplateType type, final String fileName,
                final ProjectKeeperModule module) {
            this.template = template;
            this.type = type;
            this.fileName = fileName;
            this.module = module;
        }

        public static FileTemplate fromResource(final Resource templateResource) {
            final String resourceName = templateResource.getURI().toString();
            final List<String> resourcePath = Arrays.asList(resourceName.split("/"));
            final int templatesFolderIndex = getTemplatesFolderIndex(resourcePath);
            if (isTemplatePathSyntax(resourcePath, templatesFolderIndex)) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-1")
                        .message("Template name had invalid format.").ticketMitigation().toString());
            }
            final var pathRelativeToTemplates = resourcePath.subList(templatesFolderIndex + 1, resourcePath.size());
            final var module = ProjectKeeperModule.getModuleByName(pathRelativeToTemplates.get(0));
            final var templateType = TemplateType.fromString(pathRelativeToTemplates.get(1));
            final var fileName = String.join(File.separator,
                    pathRelativeToTemplates.subList(2, pathRelativeToTemplates.size()));
            return new FileTemplate(templateResource, templateType, fileName, module);
        }

        /**
         * Get the content of the template.
         * <p>
         * This method gets the content of the template dependant of the format of the current OS (windows / linux line
         * endings).
         * </p>
         * 
         * @return template's content
         */
        public String getContent() {
            try {
                final String templateContent = this.template.getContentAsString();
                return templateContent.replace("\r", "").replace("\n", System.lineSeparator());
            } catch (final IOException exception) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-57")
                        .message("Failed to read template {{template}}.", this.template.getPath()).ticketMitigation()
                        .toString(), exception);
            }
        }

        private static boolean isTemplatePathSyntax(final List<String> resourcePath, final int templatesFolderIndex) {
            return templatesFolderIndex == -1 || resourcePath.size() - templatesFolderIndex < 3;
        }

        private static int getTemplatesFolderIndex(final List<String> resourcePath) {
            return resourcePath.indexOf("templates");
        }
    }
}
