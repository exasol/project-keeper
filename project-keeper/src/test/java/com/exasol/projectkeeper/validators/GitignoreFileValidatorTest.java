package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest->dsn~gitignore-validator~1]
class GitignoreFileValidatorTest {
    @TempDir
    Path tempDir;

    @Test
    void testValidationFileMissing() {
        final GitignoreFileValidator validator = getValidator(this.tempDir);
        assertThat(validator, validationErrorMessages(
                hasItems(startsWith("E-PK-CORE-56: Could not find required file '.gitignore'."))));
    }

    @Test
    void testValidationWrongContent() throws IOException {
        Files.writeString(this.tempDir.resolve(".gitignore"), "");
        final GitignoreFileValidator validator = getValidator(this.tempDir);
        assertThat(validator, validationErrorMessages(hasItems(startsWith(
                "E-PK-CORE-76: Invalid content of .gitignore. Please add the following required entries: ['.DS_Store', '*.swp', 'local',"))));
    }

    @Test
    void testCreateFile() throws IOException {
        getValidator(this.tempDir).validate().forEach(FindingFixHelper::fix);
        final String gitignore = Files.readString(this.tempDir.resolve(".gitignore"));
        assertThat(gitignore, Matchers.containsString(".DS_Store"));
    }

    @Test
    void testValidAfterFix() {
        final GitignoreFileValidator validator = getValidator(this.tempDir);
        validator.validate().forEach(FindingFixHelper::fix);
        assertThat(validator, hasNoValidationFindings());
    }

    @Test
    void testPartialFix() throws IOException {
        final Path gitignore = this.tempDir.resolve(".gitignore");
        Files.writeString(gitignore, "my_custom_entry\n");
        getValidator(this.tempDir).validate().forEach(FindingFixHelper::fix);
        final String content = Files.readString(gitignore);
        assertAll(//
                () -> assertThat("Custom entries should not get removed", content, containsString("my_custom_entry")),
                () -> assertThat("Missing entries should be added", content, containsString(".DS_Store"))//
        );
    }

    private GitignoreFileValidator getValidator(final Path tempDir) {
        return new GitignoreFileValidator(tempDir);
    }
}