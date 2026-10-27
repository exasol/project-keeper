package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasValidationFindingWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest->dsn~deleted-files-validator~2]
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
                + "release_droid_prepare_original_checksum.yml' exists but must not exist. Reason: Release-droid workflows are replaced by release.yml"));
    }

    @Test
    void testValidationWithDependencyUpdateWorkflow(@TempDir final Path tempDir) throws IOException {
        final Path file = tempDir.resolve(".github/workflows/dependencies_update.yml");
        Files.createDirectories(file.getParent());
        Files.createFile(file);
        assertThat(new DeletedFilesValidator(tempDir), hasValidationFindingWithMessage("E-PK-CORE-26: '.github"
                + File.separator + "workflows" + File.separator
                + "dependencies_update.yml' exists but must not exist. Reason: The dependency update workflow was removed; "
                + "vulnerabilities are monitored by Dependabot"));
    }

    @Test
    void testFix(@TempDir final Path tempDir) throws IOException {
        createdIllegalFile(tempDir);
        assertThat(new DeletedFilesValidator(tempDir), hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testFixDeletesDependencyUpdateWorkflow(@TempDir final Path tempDir) throws IOException {
        final Path file = tempDir.resolve(".github/workflows/dependencies_update.yml");
        Files.createDirectories(file.getParent());
        Files.createFile(file);
        assertThat(new DeletedFilesValidator(tempDir), hasNoMoreFindingsAfterApplyingFixes());
        assertThat(Files.exists(file), is(false));
    }

    private void createdIllegalFile(final Path tempDir) throws IOException {
        final Path file = tempDir.resolve(Path.of(".github/workflows/release_droid_prepare_original_checksum.yml"));
        Files.createDirectories(file.getParent());
        Files.createFile(file);
    }
}
