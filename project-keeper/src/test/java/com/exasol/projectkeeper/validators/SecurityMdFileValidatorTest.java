package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// [utest -> dsn~security.md-file-validator~1]
class SecurityMdFileValidatorTest {
    private static final String REPO_NAME = "repo-name";
    @TempDir
    Path projectDir;

    @Test
    void createsMissingFile() throws IOException {
        getValidator(projectDir).validate().forEach(FindingFixHelper::fix);
        assertSecurityMdFile(projectDir);
        assertThat(getValidator(projectDir).validate(), empty());
    }

    @Test
    void overwritesWrongContent() throws IOException {
        Files.writeString(projectDir.resolve("SECURITY.md"), "invalid content");
        getValidator(projectDir).validate().forEach(FindingFixHelper::fix);
        assertSecurityMdFile(projectDir);
        assertThat(getValidator(projectDir).validate(), empty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "\n", "\r\n", "\r" })
    void acceptsAlternativeNewLine(final String newline) throws IOException {
        final String contentWithAlternativeNewline = getSecurityMdContent().replace("\n", newline);
        Files.writeString(projectDir.resolve("SECURITY.md"), contentWithAlternativeNewline);
        assertThat(getValidator(projectDir).validate(), empty());
    }

    private String getSecurityMdContent() {
        return new SecurityMdFileValidator(projectDir, REPO_NAME).getTemplate();
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
