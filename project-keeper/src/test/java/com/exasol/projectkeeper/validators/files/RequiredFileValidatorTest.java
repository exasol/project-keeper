package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.hasNoValidationFindings;
import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static com.exasol.projectkeeper.validators.files.RequiredFileValidator.withContentEqualTo;
import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Validator;
import com.exasol.projectkeeper.validators.FindingFixHelper;
import com.exasol.projectkeeper.validators.finding.ValidationFinding;

class RequiredFileValidatorTest {
    private static final String EXPECTED_CONTENT = "expected content\n";

    @TempDir
    Path projectDirectory;

    @Test
    void reportsAndFixesMissingFile() throws IOException {
        final Path file = this.projectDirectory.resolve("file.txt");

        final Validator validator = validator(file, false);

        assertThat(validator, validationErrorMessages(contains("E-PK-CORE-17: Missing required file: 'file.txt'")));
        fixAllFindings(validator.validate());
        assertThat(Files.readString(file), equalTo(expectedFileContent()));
    }

    @Test
    void reportsAndFixesOutdatedContent() throws IOException {
        final Path file = this.projectDirectory.resolve("file.txt");
        Files.writeString(file, "outdated content\n");

        final Validator validator = validator(file, false);

        assertThat(validator, validationErrorMessages(contains("E-PK-CORE-18: Outdated content: 'file.txt'")));
        fixAllFindings(validator.validate());
        assertThat(Files.readString(file), equalTo(expectedFileContent()));
    }

    @Test
    void acceptsMatchingContent() throws IOException {
        final Path file = this.projectDirectory.resolve("file.txt");
        Files.writeString(file, EXPECTED_CONTENT);

        assertThat(validator(file, false), hasNoValidationFindings());
    }

    @Test
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    void fixesMissingExecutablePermissionWithoutRewritingContent() throws IOException {
        final Path file = this.projectDirectory.resolve("script.sh");
        Files.writeString(file, EXPECTED_CONTENT);
        Files.setPosixFilePermissions(file, Set.of(OWNER_READ, OWNER_WRITE));

        final Validator validator = validator(file, true);

        assertThat(validator,
                validationErrorMessages(contains("E-PK-CORE-215: Missing executable permission: 'script.sh'")));
        fixAllFindings(validator.validate());
        assertAll(() -> assertThat(Files.isExecutable(file), equalTo(true)),
                () -> assertThat(Files.readString(file), equalTo(EXPECTED_CONTENT)));
    }

    private Validator validator(final Path file, final boolean requiresExecutablePermission) {
        return () -> new RequiredFileValidator().validateFile(this.projectDirectory, file,
                withContentEqualTo(EXPECTED_CONTENT), requiresExecutablePermission);
    }

    private static String expectedFileContent() {
        return EXPECTED_CONTENT.replace("\n", System.lineSeparator());
    }

    private static void fixAllFindings(final List<ValidationFinding> findings) {
        findings.forEach(FindingFixHelper::fix);
    }
}
