package com.exasol.projectkeeper.validators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ExcludedFilesMatcher;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * {@link Validator} that checks that specific files do not exist.
 */
public class DeletedFilesValidator implements Validator {
    private static final Map<Path, String> FILES_THAT_MUST_NOT_EXIST = Map.of(//
            Path.of(".github", "workflows", "maven.yml"), "We renamed maven.yml to dependencies_check.yml", //
            Path.of("assembly", "all-dependencies.xml"),
            "We moved assembly/all-dependencies.xml to src/assembly/all-dependencies.xml"//
    );
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
    public Validator validate(final Consumer<ValidationFinding> findingConsumer) {
        FILES_THAT_MUST_NOT_EXIST.forEach((fileThatMustNotExist, reason) -> {
            final Path pathThatMustExist = this.projectDirectory.resolve(fileThatMustNotExist);
            if (this.excludedFiles.isFileExcluded(this.projectDirectory.relativize(pathThatMustExist))) {
                return;
            }
            final File file = pathThatMustExist.toFile();
            if (file.exists()) {
                findingConsumer.accept(new ValidationFinding(getFileExistsErrorMessage(fileThatMustNotExist, reason),
                        getFix(fileThatMustNotExist.toString(), file)));
            }
        });
        return this;
    }

    private String getFileExistsErrorMessage(final Path fileThatMustNotExist, final String reason) {
        return ExaError.messageBuilder("E-PK-26").message("{{FILE}} exists but must not exist. Reason: {{REASON}}")
                .parameter("FILE", fileThatMustNotExist.toString()).unquotedParameter("REASON", reason).toString();
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
