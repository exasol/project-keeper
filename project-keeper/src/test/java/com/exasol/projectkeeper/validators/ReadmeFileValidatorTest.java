package com.exasol.projectkeeper.validators;

import static com.exasol.projectkeeper.HasValidationFindingWithMessageMatcher.validationErrorMessages;
import static com.exasol.projectkeeper.ProjectKeeperModule.MAVEN_CENTRAL;
import static com.exasol.projectkeeper.ProjectKeeperModule.NATIVE_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;

// [utest->dsn~readme-validator~1]
class ReadmeFileValidatorTest {
    @Test
    void testCreateFileForSimpleMavenProject(@TempDir final Path tempDir) throws IOException {
        getValidator(tempDir, getSimpleMavenProject()).validate().forEach(FindingFixHelper::fix);
        final String readme = Files.readString(tempDir.resolve("README.md"));
        assertThat(readme, Matchers.equalTo(adaptLineSeparators("# My Project\n" + "\n"
                + "[![Build Status](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml)\n"
                + "[![Maven Central – my project](https://img.shields.io/maven-central/v/com.exasol/my-project)](https://search.maven.org/artifact/com.exasol/my-project)\n"
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
                + "* [Dependencies](dependencies.md)")));
    }

    @Test
    void testCreateFileForNativeImageProject(@TempDir final Path tempDir) throws IOException {
        getValidator(tempDir, getNativeImageMavenProject()).validate().forEach(FindingFixHelper::fix);
        final String readme = Files.readString(tempDir.resolve("README.md"));
        assertThat(readme, containsString(adaptLineSeparators(
                "\n* [Developers guide for native-image projects](https://github.com/exasol/project-keeper/blob/main/doc/developers_guide/preparing_a_project_for_native_image_builds.md)")));
    }

    @Test
    void testCreateFileForMavenMultiModuleProject(@TempDir final Path tempDir) throws IOException {
        getValidator(tempDir, getMavenMultiModuleProject()).validate().forEach(FindingFixHelper::fix);
        final String readme = Files.readString(tempDir.resolve("README.md"));
        assertThat(readme, Matchers.startsWith(adaptLineSeparators("# My Project\n" + "\n"
                + "[![Build Status](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/my-project-repo/actions/workflows/ci-build.yml)\n"
                + "sub 1: [![Maven Central – sub 1](https://img.shields.io/maven-central/v/com.exasol/sub-1)](https://search.maven.org/artifact/com.exasol/sub-1), sub 2: [![Maven Central – sub 2](https://img.shields.io/maven-central/v/com.exasol/sub-2)](https://search.maven.org/artifact/com.exasol/sub-2)\n")));
    }

    private String adaptLineSeparators(final String string) {
        return string.replace("\n", System.lineSeparator());
    }

    private ReadmeFileValidator getValidator(final Path tempDir, final List<AnalyzedSource> sources) {
        return new ReadmeFileValidator(tempDir, "My Project", "my-project-repo", sources);
    }

    private List<AnalyzedSource> getSimpleMavenProject() {
        return List.of(createSourceBuilder("my-project", true, true).build());
    }

    private List<AnalyzedSource> getNativeImageMavenProject() {
        return List.of(createSourceBuilder("my-project", true, true).modules(Set.of(NATIVE_IMAGE)).build());
    }

    private List<AnalyzedSource> getMavenMultiModuleProject() {
        return List.of(createSourceBuilder("aggregator", true, false).build(),
                createSourceBuilder("sub-1", false, true).build(), createSourceBuilder("sub-2", false, true).build());
    }

    private AnalyzedMavenSource.AnalyzedMavenSourceBuilder createSourceBuilder(final String id, final boolean isRoot,
            final boolean advertise) {
        return AnalyzedMavenSource.builder().modules(Set.of(MAVEN_CENTRAL)).advertise(advertise).artifactId(id)
                .projectName(id.replace("-", " ")).isRootProject(isRoot);
    }

    @Test
    void testValidation(@TempDir final Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("README.md"), "");
        final ReadmeFileValidator validator = getValidator(tempDir, getSimpleMavenProject());
        assertThat(validator, validationErrorMessages(hasItems(
                containsString("The project's README.md does not reference the dependencies.md file."), //
                startsWith(
                        "E-PK-CORE-62: The project's README.md does not contain a valid badges block. Please add or replace the following badges: \n'[![Build Status]"))));
    }

    @Test
    void testNativeImageDevGuideValidation(@TempDir final Path tempDir) throws IOException {
        Files.writeString(tempDir.resolve("README.md"), "");
        final ReadmeFileValidator validator = getValidator(tempDir, getNativeImageMavenProject());
        assertThat(validator, validationErrorMessages(hasItems(containsString(
                "E-PK-CORE-144: The project's README.md does not reference the developers guide for native images.' Please add the link: '[Developers guide for native-image projects](https://github.com/exasol/project-keeper/blob/main/doc/developers_guide/preparing_a_project_for_native_image_builds.md)'."))));
    }
}