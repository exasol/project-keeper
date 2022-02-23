package com.exasol.projectkeeper.validators.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

import lombok.RequiredArgsConstructor;

/**
 * This class validates that a required file exists and checks its content.
 */
public class RequiredFileValidator {

    /**
     * Create a {@link ContentValidator} for an expected content.
     * 
     * @param content expected content
     * @return {@link ContentValidator}
     */
    public static ContentValidator withContentEqualTo(final String content) {
        return new EqualsContentValidator(content);
    }

    private static void fixFile(final Path file, final String templateContent) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, templateContent);
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", file).toString(),
                    exception);
        }
    }

    /**
     * Validate a file.
     *
     * @param projectDir       project directory
     * @param file             absolute path of the file to validate
     * @param contentValidator validator for the content
     * @return validation findings
     */
    public List<ValidationFinding> validateFile(final Path projectDir, final Path file,
            final ContentValidator contentValidator) {
        final String content = getActualContent(projectDir, file);
        final Optional<String> newContent = contentValidator.validateContent(content);
        if (newContent.isPresent()) {
            final SimpleValidationFinding.Fix fix = (Logger log) -> fixFile(file, newContent.get());
            if (content == null) {
                return List.of(SimpleValidationFinding
                        .withMessage(
                                ExaError.messageBuilder("E-PK-CORE-17").message("Missing required file: '{{required file}}'")
                                        .parameter("required file", projectDir.relativize(file)).toString())
                        .andFix(fix).build());
            } else {
                return List.of(SimpleValidationFinding
                        .withMessage(ExaError.messageBuilder("E-PK-CORE-18")
                                .message("Outdated content: '{{file name}}'", projectDir.relativize(file)).toString())
                        .andFix(fix).build());
            }
        } else {
            return Collections.emptyList();
        }
    }

    private String getActualContent(final Path projectDir, final Path file) {
        if (!Files.exists(file)) {
            return null;
        } else {
            try {
                return Files.readString(file);
            } catch (final IOException exception) {
                throw new IllegalStateException(
                        ExaError.messageBuilder("E-PK-CORE-19").message("Failed to validate {{file name}}'s content.")
                                .parameter("file name", projectDir.relativize(file)).toString(),
                        exception);
            }
        }
    }

    /**
     * Interface for classes that validate the content of a file.
     */
    @FunctionalInterface
    public interface ContentValidator {
        /**
         * Validate the content of a file.
         *
         * @param content content to validate, null if file does not exist
         * @return expected content if content is invalid, empty optional otherwise
         */
        public Optional<String> validateContent(String content);
    }

    @RequiredArgsConstructor
    private static class EqualsContentValidator implements ContentValidator {
        private final String expectedContent;

        @Override
        public Optional<String> validateContent(final String content) {
            if (!this.expectedContent.equals(content)) {
                return Optional.of(this.expectedContent);
            } else {
                return Optional.empty();
            }
        }
    }
}
