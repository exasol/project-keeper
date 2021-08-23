package com.exasol.projectkeeper.validators.changelog;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ExcludedFilesMatcher;
import com.exasol.projectkeeper.ValidationFinding;

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
        assertThat(findings.get(0).getMessage(),
                startsWith("E-PK-69: The changelog.md file has an outdated content. Expected content:"));
    }

    private ChangelogFileValidator getValidator() {
        return new ChangelogFileValidator(this.tempDir, new ExcludedFilesMatcher(Collections.emptyList()));
    }

    @Test
    void testFix() {
        getValidator().validate().forEach(finding -> finding.getFix().fixError(mock(Log.class)));
        final List<ValidationFinding> findingsInSecondRun = getValidator().validate();
        assertThat(findingsInSecondRun.size(), equalTo(0));
    }
}