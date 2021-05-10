package com.exasol.projectkeeper.validators.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ExcludedFilesMatcher;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;

/**
 * Validator for the projects file structure.
 */
public class ProjectFilesValidator implements Validator {
    private static final InputStreamComparator COMPARATOR = new InputStreamComparator();
    private final Collection<ProjectKeeperModule> enabledModules;
    private final File projectDirectory;
    private final ExcludedFilesMatcher excludedFilesMatcher;

    /**
     * Crate a new instance of {@link ProjectFilesValidator}.
     * 
     * @param enabledModules       list of enabled {@link ProjectKeeperModule}s
     * @param projectDirectory     project's root directory
     * @param excludedFilesMatcher files to exclude from validation
     */
    public ProjectFilesValidator(final Collection<ProjectKeeperModule> enabledModules, final File projectDirectory,
            final ExcludedFilesMatcher excludedFilesMatcher) {
        this.enabledModules = enabledModules;
        this.projectDirectory = projectDirectory;
        this.excludedFilesMatcher = excludedFilesMatcher;
    }

    @Override
    public List<ValidationFinding> validate() {
        try (final var scanResult = new ClassGraph().acceptPaths("templates/").scan()) {
            return scanResult.getAllResources().stream()//
                    .map(FileTemplate::fromResource)//
                    .filter(template -> this.enabledModules.contains(template.module))
                    .filter(template -> !this.excludedFilesMatcher.isFileExcluded(Path.of(template.fileName)))
                    .flatMap(template -> validate(template).stream())//
                    .collect(Collectors.toList());
        }
    }

    private List<ValidationFinding> validate(final FileTemplate template) {
        final var projectFile = this.projectDirectory.toPath().resolve(template.fileName).toFile();
        if (!projectFile.exists()) {
            return List.of(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-17").message("Missing required: {{required file}}")
                            .parameter("required file", template.fileName).toString())
                    .andFix((Log log) -> fixFile(projectFile, template.template.getPath())).build());
        }
        if (template.type.equals(TemplateType.REQUIRE_EXACT)) {
            return validateContent(template, projectFile);
        } else {
            return Collections.emptyList();
        }
    }

    private void fixFile(final File projectFile, final String templateFilePath) {
        try (final InputStream templateStream = getClass().getClassLoader().getResourceAsStream(templateFilePath)) {
            projectFile.mkdirs();
            Files.copy(templateStream, projectFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", projectFile.getAbsolutePath()).toString(),
                    exception);
        }
    }

    private List<ValidationFinding> validateContent(final FileTemplate template, final File projectFile) {
        if (!isFileEqualWithTemplate(template, projectFile)) {
            return List.of(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-18").message("Outdated content: {{file name}}")
                            .parameter("file name", template.fileName).toString())
                    .andFix((Log log) -> fixFile(projectFile, template.template.getPath())).build());
        } else {
            return Collections.emptyList();
        }
    }

    private boolean isFileEqualWithTemplate(final FileTemplate template, final File projectFile) {
        try (template.template;
                final InputStream templateStream = template.template.open();
                final var actualInputStream = new FileInputStream(projectFile)) {
            return COMPARATOR.areStreamsEqual(actualInputStream, templateStream);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-19").message("Failed to validate {{file name}}'s content.")
                            .parameter("file name", template.fileName).toString(),
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
                        ExaError.messageBuilder("F-PK-3").message("Unknown template type {{type}}.")
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
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-1")
                        .message("Template name had invalid format.").ticketMitigation().toString());
            }
            final var pathRelativeToTemplates = resourcePath.subList(templatesFolderIndex + 1, resourcePath.size());
            final var module = ProjectKeeperModule.getModuleByName(pathRelativeToTemplates.get(0));
            final var templateType = TemplateType.fromString(pathRelativeToTemplates.get(1));
            final var fileName = String.join(File.separator,
                    pathRelativeToTemplates.subList(2, pathRelativeToTemplates.size()));
            return new FileTemplate(templateResource, templateType, fileName, module);
        }

        private static boolean isTemplatePathSyntax(final List<String> resourcePath, final int templatesFolderIndex) {
            return templatesFolderIndex == -1 || resourcePath.size() - templatesFolderIndex < 3;
        }

        private static int getTemplatesFolderIndex(final List<String> resourcePath) {
            return resourcePath.indexOf("templates");
        }
    }
}
