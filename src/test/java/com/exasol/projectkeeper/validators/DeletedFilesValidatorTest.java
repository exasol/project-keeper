package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ExcludedFilesMatcher;

class DeletedFilesValidatorTest {

    @Test
    void testValidation(@TempDir final Path tempDir) {
        assertThat(new DeletedFilesValidator(tempDir, new ExcludedFilesMatcher(Collections.emptyList())),
                hasNoValidationFindings());
    }

    @Test
    void testValidationWithFileThatMustNotExist(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir, new ExcludedFilesMatcher(Collections.emptyList())),
                hasValidationFindingWithMessage("E-PK-26: '.github" + File.separator + "workflows" + File.separator
                        + "maven.yml' exists but must not exist. Reason: We renamed maven.yml to dependencies_check.yml"));
    }

    @Test
    void testValidationWithExclusion(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir, new ExcludedFilesMatcher(List.of(".github/workflows/maven.yml"))),
                hasNoValidationFindings());
    }

    @Test
    void testFix(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir, new ExcludedFilesMatcher(Collections.emptyList())),
                hasNoMoreFindingsAfterApplyingFixes());
    }

    private void createdIllegalFile(final Path tempDir) throws IOException {
        final File file = tempDir.resolve(Path.of(".github", "workflows", "maven.yml")).toFile();
        file.mkdirs();
        file.createNewFile();
    }
}