package com.exasol.projectkeeper.validators.files;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

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
            Files.writeString(file, templateContent.replace("\r", "").replace("\n", System.lineSeparator()));
        } catch (final IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-PK-CORE-16").message("Failed to create or replace {{required file}}.")
                            .parameter("required file", file).toString(),
                    exception);
        }
    }

    private static String unifyNewlines(final String content) {
        if (content == null) {
            return null;
        } else {
            return content.replace("\r", "");
        }
    }

    /**
     * Validate that a file exists and has exactly the expected content.
     *
     * @param projectDir       project directory
     * @param file             absolute path of the file to validate
     * @param contentValidator validator for the content
     * @return validation findings
     */
    public List<ValidationFinding> validateFile(final Path projectDir, final Path file,
            final ContentValidator contentValidator) {
        final String content = getActualContent(projectDir, file);
        final String contentWithUnifiedNewline = unifyNewlines(content);
        final Optional<String> newContent = contentValidator.validateContent(contentWithUnifiedNewline);
        if (newContent.isPresent()) {
            final SimpleValidationFinding.Fix fix = (final Logger log) -> fixFile(file, newContent.get());
            if (contentWithUnifiedNewline == null) {
                return List.of(SimpleValidationFinding
                        .withMessage(ExaError.messageBuilder("E-PK-CORE-17")
                                .message("Missing required file: '{{required file}}'")
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

    /**
     * Validate that a file exists, ignoring it's content.
     *
     * @param projectDir     project directory
     * @param file           absolute path of the file to validate
     * @param defaultContent the default content in case the file does not exist
     * @return validation findings
     */
    public List<ValidationFinding> validateFileExists(final Path projectDir, final Path file,
            final String defaultContent) {
        if (!Files.exists(file)) {
            final SimpleValidationFinding.Fix fix = (final Logger log) -> fixFile(file, defaultContent);
            return List
                    .of(SimpleValidationFinding
                            .withMessage(ExaError.messageBuilder("E-PK-CORE-119")
                                    .message("Missing required file: '{{required file}}'")
                                    .parameter("required file", projectDir.relativize(file)).toString())
                            .andFix(fix).build());
        } else {
            return emptyList();
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

    private static class EqualsContentValidator implements ContentValidator {
        private final String expectedContent;

        private EqualsContentValidator(final String expectedContent) {
            this.expectedContent = unifyNewlines(expectedContent);
        }

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
