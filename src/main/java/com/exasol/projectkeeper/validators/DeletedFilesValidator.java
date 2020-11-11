package com.exasol.projectkeeper.validators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * {@link Validator} that checks that specific files do not exist.
 */
public class DeletedFilesValidator implements Validator {
    private static final Map<Path, String> FILES_THAT_MUST_NOT_EXIST = Map.of(//
            Path.of(".github", "workflows", "maven.yml"), "We renamed maven.yml to dependencies_check.yml"//
    );
    private final Path projectDirectory;

    /**
     * Create a new instance of {@link DeletedFilesValidator}.
     * 
     * @param projectDirectory project's root directory
     */
    public DeletedFilesValidator(final Path projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public Validator validate(final Consumer<ValidationFinding> findingConsumer) {
        FILES_THAT_MUST_NOT_EXIST.forEach((fileThatMustNotExist, reason) -> {
            final File file = this.projectDirectory.resolve(fileThatMustNotExist).toFile();
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
