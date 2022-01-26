package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItems;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.validators.FindingFixHelper;

//[utest->dsn~required-files-validator~1]
class ProjectFilesValidatorTest {
    private static final List<ProjectKeeperModule> PROJECT_KEEPER_MODULES = List.of(ProjectKeeperModule.DEFAULT);

    @Test
    void testValidation(@TempDir final Path tempDir) throws IOException {
        Files.createDirectories(tempDir.resolve(".settings"));
        Files.createFile(tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.ui.prefs")));
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir);
        assertThat(validator,
                validationErrorMessages(hasItems(
                        "E-PK-CORE-17: Missing required: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'",
                        "E-PK-CORE-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.ui.prefs'")));
    }

    @Test
    void testFix(@TempDir final Path tempDir) {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir);
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testDifferentContent(@TempDir final Path tempDir) throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir);
        validator.validate().forEach(FindingFixHelper::fix); // fix all findings
        final Path testFile = tempDir.resolve(".settings" + File.separator + "org.eclipse.jdt.core.prefs");
        changeFile(testFile);
        assertThat(validator, validationErrorMessages(contains(
                "E-PK-CORE-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'")));
    }

    @Test
    void testFixDifferentContent(@TempDir final Path tempDir) throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir);
        validator.validate().forEach(FindingFixHelper::fix); // fix all findings
        final Path testFile = tempDir.resolve(Path.of(".settings", "org.eclipse.jdt.core.prefs"));
        changeFile(testFile);
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    private void changeFile(final Path testFile) throws IOException {
        Files.writeString(testFile, "something");
    }
}