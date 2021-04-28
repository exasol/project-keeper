package com.exasol.projectkeeper.validators.files;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;

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

    public List<ValidationFinding> validate(final FileTemplate template) {
        final var projectFile = this.projectDirectory.toPath().resolve(template.fileName).toFile();
        if (!projectFile.exists()) {
            return List.of(new ValidationFinding(
                    ExaError.messageBuilder("E-PK-17").message("Missing required: {{required file}}")
                            .parameter("required file", template.fileName).toString(),
                    (Log log) -> fixFile(projectFile, template.template.getPath())));
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
            return List.of(new ValidationFinding(
                    ExaError.messageBuilder("E-PK-18").message("Outdated content: {{file name}}")
                            .parameter("file name", template.fileName).toString(),
                    (Log log) -> fixFile(projectFile, template.template.getPath())));
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
            final var resourceName = templateResource.getURI().toString();
            final var resourcePath = Path.of(resourceName);
            final int templatesFolderIndex = getTemplatesFolderIndex(resourcePath);
            if (isTemplatePathSyntax(resourcePath, templatesFolderIndex)) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-1")
                        .message("Template name had invalid format.").ticketMitigation().toString());
            }
            final var pathRelativeToTemplates = resourcePath.subpath(templatesFolderIndex + 1,
                    resourcePath.getNameCount());
            final var module = ProjectKeeperModule.getModuleByName(pathRelativeToTemplates.getName(0).toString());
            final var templateType = TemplateType.fromString(pathRelativeToTemplates.getName(1).toString());
            final var fileName = pathRelativeToTemplates.subpath(2, pathRelativeToTemplates.getNameCount()).toString();
            return new FileTemplate(templateResource, templateType, fileName, module);
        }

        private static boolean isTemplatePathSyntax(final Path resourcePath, final int templatesFolderIndex) {
            return templatesFolderIndex == -1 || resourcePath.getNameCount() - templatesFolderIndex < 3;
        }

        private static int getTemplatesFolderIndex(final Path resourcePath) {
            for (var index = 0; index < resourcePath.getNameCount(); index++) {
                if (resourcePath.getName(index).toString().equals("templates")) {
                    return index;
                }
            }
            return -1;
        }
    }
}
