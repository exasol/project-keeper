package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

// [utest->dsn~readme-validator~1]
class ReadmeValidatorTest {
    @Test
    void testCreateFile(@TempDir final Path tempDir) throws IOException {
        getValidator(tempDir).validate().forEach(finding -> finding.getFix().fixError(mock(Log.class)));
        final String readme = Files.readString(tempDir.resolve("README.md"));
        assertThat(readme, Matchers.equalTo("# My Project\n" + "\n"
                + "[![Build Status](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml)\n"
                + "[![Maven Central](https://img.shields.io/maven-central/v/com.exasol/my-project)](https://search.maven.org/artifact/com.exasol/project-keeper-maven-pluginmy-project)\n"
                + "\n"
                + "[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "\n"
                + "[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "\n"
                + "[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Amy-project&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Amy-project)\n"
                + "\n" + "## Additional Information\n" + "\n" + "* [Changelog](doc/changes/changelog.md)\n"
                + "* [Dependencies](dependencies.md)"));
    }

    private ReadmeValidator getValidator(final Path tempDir) {
        return new ReadmeValidator(tempDir, "My Project", "my-project", "my-project-repo", List.of(MAVEN_CENTRAL));
    }

    @Test
    void testValidation(@TempDir final Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("README.md"), "");
        final ReadmeValidator validator = getValidator(tempDir);
        assertThat(validator, validationErrorMessages(hasItems(
                containsString("The project's README.md does not reference the dependencies.md file."), //
                startsWith(
                        "E-PK-62: The project's README.md does not contain a valid badges block. Please add or replace the following badges: \n"
                                + "'[![Build Status]"))));
    }
}