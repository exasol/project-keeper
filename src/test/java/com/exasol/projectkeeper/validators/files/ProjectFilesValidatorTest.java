package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasNoMoreFindingsAfterApplyingFixesMatcher.hasNoMoreFindingsAfterApplyingFixes;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

import java.io.*;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ExcludedFilesMatcher;
import com.exasol.projectkeeper.ProjectKeeperModule;

//[utest->dsn~required-files-validator~1]
class ProjectFilesValidatorTest {
    private static final List<ProjectKeeperModule> PROJECT_KEEPER_MODULES = List.of(ProjectKeeperModule.DEFAULT);

    @Test
    void testValidation(@TempDir final File tempDir) throws IOException {
        if (!tempDir.toPath().resolve(".settings").toFile().mkdir() || !tempDir.toPath()
                .resolve(Path.of(".settings", "org.eclipse.jdt.ui.prefs")).toFile().createNewFile()) {
            throw new IllegalStateException("Failed to create test files.");
        }
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir,
                new ExcludedFilesMatcher(Collections.emptyList()));
        assertThat(validator,
                validationErrorMessages(hasItems(
                        "E-PK-17: Missing required: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'",
                        "E-PK-17: Missing required: 'README.md'",
                        "E-PK-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.ui.prefs'")));
    }

    @Test
    void testValidationWithExclusion(@TempDir final File tempDir) {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir,
                new ExcludedFilesMatcher(List.of(".settings/org.eclipse.jdt.core.prefs")));
        assertThat(validator, validationErrorMessages(
                not(hasItems("E-PK-17: Missing required: .settings" + File.separator + "org.eclipse.jdt.core.prefs"))));
    }

    @Test
    void testFix(@TempDir final File tempDir) {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir,
                new ExcludedFilesMatcher(Collections.emptyList()));
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    @Test
    void testDifferentContent(@TempDir final File tempDir) throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir,
                new ExcludedFilesMatcher(Collections.emptyList()));
        validator.validate().forEach(finding -> finding.getFix().fixError(mock(Log.class))); // fix all findings
        final File testFile = tempDir.toPath().resolve(".settings" + File.separator + "org.eclipse.jdt.core.prefs")
                .toFile();
        changeFile(testFile);
        assertThat(validator, validationErrorMessages(
                contains("E-PK-18: Outdated content: '.settings" + File.separator + "org.eclipse.jdt.core.prefs'")));
    }

    @Test
    void testFixDifferentContent(@TempDir final File tempDir) throws IOException {
        final ProjectFilesValidator validator = new ProjectFilesValidator(PROJECT_KEEPER_MODULES, tempDir,
                new ExcludedFilesMatcher(Collections.emptyList()));
        validator.validate().forEach(finding -> finding.getFix().fixError(mock(Log.class))); // fix all findings
        final File testFile = tempDir.toPath().resolve(Path.of(".settings", "org.eclipse.jdt.core.prefs")).toFile();
        changeFile(testFile);
        assertThat(validator, hasNoMoreFindingsAfterApplyingFixes());
    }

    private void changeFile(final File testFile) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write("something");
            fileWriter.flush();
        }
    }
}