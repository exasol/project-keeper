package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest->dsn~deleted-files-validator~1]
class DeletedFilesValidatorTest {

    @Test
    void testValidation(@TempDir final Path tempDir) {
        assertThat(new DeletedFilesValidator(tempDir), hasNoValidationFindings());
    }

    @Test
    void testValidationWithFileThatMustNotExist(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir), hasValidationFindingWithMessage("E-PK-CORE-26: '.github"
                + File.separator + "workflows" + File.separator
                + "maven.yml' exists but must not exist. Reason: We renamed maven.yml to dependencies_check.yml"));
    }

    @Test
    void testFix(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir), hasNoMoreFindingsAfterApplyingFixes());
    }

    private void createdIllegalFile(final Path tempDir) throws IOException {
        final Path file = tempDir.resolve(Path.of(".github", "workflows", "maven.yml"));
        Files.createDirectories(file.getParent());
        Files.createFile(file);
    }
}