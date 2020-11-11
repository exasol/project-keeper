package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DeletedFilesValidatorTest {

    @Test
    void testValidation(@TempDir final Path tempDir) {
        assertThat(new DeletedFilesValidator(tempDir), hasNoValidationFindings());
    }

    @Test
    void testValidationWithFileThatMustNotExist(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir), hasValidationFindingWithMessage(
                "E-PK-26: '.github/workflows/maven.yml' exists but must not exist. Reason: We renamed maven.yml to dependencies_check.yml"));
    }

    @Test
    void testFix(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir), hasNoMoreFindingsAfterApplyingFixes());
    }

    private File createdIllegalFile(final Path tempDir) throws IOException {
        final File file = tempDir.resolve(Path.of(".github", "workflows", "maven.yml")).toFile();
        file.mkdirs();
        file.createNewFile();
        return file;
    }
}