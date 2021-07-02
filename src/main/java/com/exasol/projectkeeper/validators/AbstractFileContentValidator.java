package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * This class is a abstract basis for {@link Validator}s that validate a files content as string.
 */
public abstract class AbstractFileContentValidator extends AbstractFileValidator {
    /**
     * Create a new instance of {@link AbstractFileContentValidator}.
     *
     * @param projectDirectory project's root directory
     * @param filePath         path of the file to validate relative to projectDirectory
     */
    protected AbstractFileContentValidator(final Path projectDirectory, final Path filePath) {
        super(projectDirectory, filePath);
    }

    @Override
    protected final List<ValidationFinding> validateContent(final Path file) {
        try {
            final String content = Files.readString(file);
            return validateContent(content);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-60")
                    .message("Failed to read file {{file name}} for validation.", file).toString());
        }
    }

    protected abstract List<ValidationFinding> validateContent(final String content);

    @Override
    protected final void writeTemplateFile(final Path target) throws IOException {
        Files.writeString(target, getTemplate());
    }

    protected abstract String getTemplate();
}
