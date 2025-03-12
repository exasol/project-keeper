package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest -> dsn~security.md-file-validator~1]
class SecurityMdFileValidatorTest {
    private static final String REPO_NAME = "repo-name";

    @Test
    void createsMissingFile(@TempDir final Path projectDir) throws IOException {
        getValidator(projectDir).validate().forEach(FindingFixHelper::fix);
        assertSecurityMdFile(projectDir);
        assertThat(getValidator(projectDir).validate(), empty());
    }

    @Test
    void overwritesWrongContent(@TempDir final Path projectDir) throws IOException {
        Files.writeString(projectDir.resolve("SECURITY.md"), "invalid content");
        getValidator(projectDir).validate().forEach(FindingFixHelper::fix);
        assertSecurityMdFile(projectDir);
        assertThat(getValidator(projectDir).validate(), empty());
    }

    private void assertSecurityMdFile(final Path projectDir) throws IOException {
        assertThat(Files.readString(projectDir.resolve("SECURITY.md")), allOf(
                startsWith("# Security\n"),
                containsString("https://github.com/exasol/repo-name/security/advisories/new"),
                endsWith("We are happy to acknowledge your research publicly when possible.\n")));
    }

    private SecurityMdFileValidator getValidator(final Path tempDir) {
        return new SecurityMdFileValidator(tempDir, REPO_NAME);
    }
}
