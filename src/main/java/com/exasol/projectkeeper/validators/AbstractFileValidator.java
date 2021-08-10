package com.exasol.projectkeeper.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ValidationFinding;
import com.exasol.projectkeeper.Validator;

/**
 * This class is a abstract basis for {@link Validator}s that validate files.
 */
public abstract class AbstractFileValidator implements Validator {
    private final Path absoluteFilePath;
    private final Path relativeFilePath;

    /**
     * Create a new instance of {@link AbstractFileValidator}.
     * 
     * @param projectDirectory project's root directory
     * @param filePath         path of the file to validate relative to projectDirectory
     */
    protected AbstractFileValidator(final Path projectDirectory, final Path filePath) {
        this.relativeFilePath = filePath;
        this.absoluteFilePath = projectDirectory.resolve(filePath);
    }

    @Override
    public final List<ValidationFinding> validate() {
        if (isValidationEnabled()) {
            return runValidation();
        } else {
            return Collections.emptyList();
        }
    }

    private List<ValidationFinding> runValidation() {
        if (!Files.exists(this.absoluteFilePath)) {
            return List.of(getMissingFileFinding());
        } else {
            return validateContent(this.absoluteFilePath);
        }
    }

    private ValidationFinding getMissingFileFinding() {
        return ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-56")
                .message("Could not find required file {{file name}}.")
                .parameter("file name", this.relativeFilePath.toString(), "Name of the required file.").toString())
                .andFix(getCreateFileFix()).build();
    }

    protected ValidationFinding.Fix getCreateFileFix() {
        return (Log log) -> {
            try {
                Files.createDirectories(this.absoluteFilePath.getParent());
                writeTemplateFile(this.absoluteFilePath);
                log.warn("Created '" + this.relativeFilePath + "'. Don't forget to update it's content!");
            } catch (final IOException exception) {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-63")
                        .message("Failed to create required file {{file path}}.", this.relativeFilePath).toString());
            }
        };
    }

    /**
     * Create a new file with a template content.
     * <p>
     * The {@link AbstractFileValidator} calls this method when it applies the fix for a missing file.
     * </p>
     * 
     * @param target file to create
     * @throws IOException if creation fails
     */
    protected abstract void writeTemplateFile(Path target) throws IOException;

    /**
     * Validate the content of the file.
     * 
     * @param file absolute path of the file to validate
     * @return validation findings
     */
    protected abstract List<ValidationFinding> validateContent(Path file);

    /**
     * This methods allows subclasses to control if validation should be enabled or not.
     * 
     * @return {@code true} if validation is enabled
     */
    protected boolean isValidationEnabled() {
        return true;
    }
}
