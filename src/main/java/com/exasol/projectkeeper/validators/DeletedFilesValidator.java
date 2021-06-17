package com.exasol.projectkeeper.validators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;

/**
 * {@link Validator} that checks that specific files do not exist.
 */
// [impl->dsn~deleted-files-validator~1]
public class DeletedFilesValidator implements Validator {
    private static final String GITHUB_WORKFLOWS = ".github/workflows";
    private static final Map<Path, String> FILES_THAT_MUST_NOT_EXIST = Map.of(//
            Path.of(GITHUB_WORKFLOWS, "maven.yml"), "We renamed maven.yml to dependencies_check.yml", //
            Path.of("assembly", "all-dependencies.xml"),
            "We moved assembly/all-dependencies.xml to src/assembly/all-dependencies.xml",
            Path.of(GITHUB_WORKFLOWS, "maven_central_release.yml"),
            "We renamed maven.yml to release_droid_release_on_maven_central.yml",
            Path.of(GITHUB_WORKFLOWS, "github_release.yml"),
            "We renamed maven.yml to release_droid_upload_github_release_assets.yml");
    private final Path projectDirectory;
    private final ExcludedFilesMatcher excludedFiles;

    /**
     * Create a new instance of {@link DeletedFilesValidator}.
     *
     * @param projectDirectory project's root directory
     * @param excludedFiles    matcher for explicitly excluded files
     */
    public DeletedFilesValidator(final Path projectDirectory, final ExcludedFilesMatcher excludedFiles) {
        this.projectDirectory = projectDirectory;
        this.excludedFiles = excludedFiles;
    }

    @Override
    public List<ValidationFinding> validate() {
        return FILES_THAT_MUST_NOT_EXIST.entrySet().stream().map(entry -> {
            final var fileThatMustNotExist = entry.getKey();
            final String reason = entry.getValue();
            final Path pathThatMustExist = this.projectDirectory.resolve(fileThatMustNotExist);
            if (this.excludedFiles.isFileExcluded(this.projectDirectory.relativize(pathThatMustExist))) {
                return null;
            }
            final var file = pathThatMustExist.toFile();
            if (file.exists()) {
                return ValidationFinding.withMessage(getFileExistsErrorMessage(fileThatMustNotExist, reason))
                        .andFix(getFix(fileThatMustNotExist.toString(), file)).build();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String getFileExistsErrorMessage(final Path fileThatMustNotExist, final String reason) {
        return ExaError.messageBuilder("E-PK-26").message("{{FILE}} exists but must not exist. Reason: {{REASON|uq}}",
                fileThatMustNotExist.toString(), reason).toString();
    }

    private ValidationFinding.Fix getFix(final String fileName, final File file) {
        return log -> {
            try {
                Files.delete(file.toPath());
            } catch (final IOException exception) {
                throw new IllegalStateException(
                        ExaError.messageBuilder("E-PK-27").message("Failed to delete {{FILE}}.")
                                .parameter("FILE", fileName).mitigation("Check file permissions.").toString(),
                        exception);
            }
        };
    }
}
