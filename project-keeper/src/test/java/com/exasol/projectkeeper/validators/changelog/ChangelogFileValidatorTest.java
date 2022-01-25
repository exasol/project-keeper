package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.validators.finding.*;

//[utest->dsn~verify-changelog-file~1]
class ChangelogFileValidatorTest {
    @TempDir
    Path tempDir;

    @BeforeEach
    void beforeEach() throws IOException {
        final Path changesDirectory = this.tempDir.resolve(Path.of("doc", "changes"));
        Files.createDirectories(changesDirectory);
        Files.createFile(changesDirectory.resolve("changes_1.0.0.md"));
        Files.createFile(changesDirectory.resolve("changelog.md")); // leve empty so it's invalid
    }

    @Test
    void testValidateWrongContent() {
        final List<ValidationFinding> findings = getValidator().validate();
        assertThat(((SimpleValidationFinding) findings.get(0)).getMessage(),
                startsWith("E-PK-CORE-69: The changelog.md file has an outdated content. Expected content:"));
    }

    private ChangelogFileValidator getValidator() {
        return new ChangelogFileValidator(this.tempDir);
    }

    @Test
    void testFix() {
        final List<ValidationFinding> findings = getValidator().validate();
        new FindingsFixer().fixFindings(findings, mock(Logger.class));
        final List<ValidationFinding> findingsInSecondRun = getValidator().validate();
        assertThat(findingsInSecondRun.size(), equalTo(0));
    }
}