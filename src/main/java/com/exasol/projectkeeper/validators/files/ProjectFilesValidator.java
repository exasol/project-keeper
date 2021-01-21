package com.exasol.projectkeeper.validators.files;

import java.io.*;
import java.nio.file.*;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;

import io.github.classgraph.*;

/**
 * Validator for the projects file structure.
 */
public class ProjectFilesValidator implements Validator {
    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern
            .compile(".*\\/templates\\/([^\\/]+)\\/([^\\/]+)\\/(.*)");
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
    public ProjectFilesValidator validate(final Consumer<ValidationFinding> findingConsumer) {
        try (final ScanResult scanResult = new ClassGraph().acceptPaths("templates/").scan()) {
            scanResult.getAllResources().stream()//
                    .map(FileTemplate::fromResource)//
                    .filter(template -> this.enabledModules.contains(template.module))
                    .filter(template -> !this.excludedFilesMatcher.isFileExcluded(Path.of(template.fileName)))
                    .forEach(template -> validate(template, findingConsumer));
        }
        return this;
    }

    public void validate(final FileTemplate template, final Consumer<ValidationFinding> findingConsumer) {
        final File projectFile = this.projectDirectory.toPath().resolve(template.fileName).toFile();
        if (!projectFile.exists()) {
            findingConsumer.accept(new ValidationFinding(
                    ExaError.messageBuilder("E-PK-17").message("Missing required: {{required file}}")
                            .parameter("required file", template.fileName).toString(),
                    (Log log) -> fixFile(projectFile, template.template)));
            return;
        }
        if (template.type.equals(TemplateType.REQUIRE_EXACT)) {
            validateContent(template, findingConsumer, projectFile);
        }
    }

    private void fixFile(final File projectFile, final Resource templateFile) {
        try (templateFile; final InputStream templateStream = templateFile.open()) {
            projectFile.mkdirs();
            Files.copy(templateStream, projectFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", projectFile.getAbsolutePath()).toString(),
                    exception);
        }
    }

    private void validateContent(final FileTemplate template, final Consumer<ValidationFinding> findingConsumer,
            final File projectFile) {
        if (!isFileEqualWithTemplate(template, projectFile)) {
            findingConsumer.accept(new ValidationFinding(
                    ExaError.messageBuilder("E-PK-18").message("Outdated content: {{file name}}")
                            .parameter("file name", template.fileName).toString(),
                    (Log log) -> fixFile(projectFile, template.template)));
        }
    }

    private boolean isFileEqualWithTemplate(final FileTemplate template, final File projectFile) {
        try (template.template;
                final InputStream templateStream = template.template.open();
                final FileInputStream actualInputStream = new FileInputStream(projectFile)) {
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
            final Matcher matcher = TEMPLATE_NAME_PATTERN.matcher(resourceName);
            if (!matcher.matches()) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-1")
                        .message("Template name had invalid format.").ticketMitigation().toString());
            }
            final ProjectKeeperModule module = ProjectKeeperModule.getModuleByName(matcher.group(1));
            final TemplateType templateType = TemplateType.fromString(matcher.group(2));
            final String fileName = matcher.group(3);
            return new FileTemplate(templateResource, templateType, fileName, module);
        }
    }
}
