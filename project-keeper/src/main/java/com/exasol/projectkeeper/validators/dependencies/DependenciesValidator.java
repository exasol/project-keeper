package com.exasol.projectkeeper.validators.dependencies;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.dependencies.ProjectDependency;
import com.exasol.projectkeeper.validators.dependencies.renderer.DependencyPageRenderer;

/**
 * {@link Validator} for the dependencies.md file.
 */
//[impl->dsn~depnedency.md-file-validator~1]
public class DependenciesValidator implements Validator {
    private final Path dependenciesFile;
    private final BrokenLinkReplacer brokenLinkReplacer;
    private final JavaProjectCrawlerRunner javaProjectCrawlerRunner;

    /**
     * Create a new instance of {@link DependenciesValidator}.
     * 
     * @param pomFile               pom file to validate
     * @param projectDirectory      project root directory
     * @param brokenLinkReplacer    dependency injection for broken link replacer
     * @param mvnRepositoryOverride maven repository override. USe {@code null} for default
     * @param ownVersion            project-keeper version
     */
    public DependenciesValidator(final File pomFile, final Path projectDirectory,
            final BrokenLinkReplacer brokenLinkReplacer, final Path mvnRepositoryOverride, final String ownVersion) {
        this.brokenLinkReplacer = brokenLinkReplacer;
        this.javaProjectCrawlerRunner = new JavaProjectCrawlerRunner(pomFile.toPath(), mvnRepositoryOverride,
                ownVersion);
        this.dependenciesFile = projectDirectory.resolve("dependencies.md");
    }

    @Override
    public List<ValidationFinding> validate() {
        final String expectedDependenciesPage = generateExpectedReport();
        if (!this.dependenciesFile.toFile().exists()) {
            return List.of(ValidationFinding
                    .withMessage(ExaError.messageBuilder("E-PK-50")
                            .message("This project does not have a dependencies.md file.").toString())
                    .andFix(getFix(expectedDependenciesPage)).build());
        } else {
            return validateFileContent(expectedDependenciesPage);
        }
    }

    private String generateExpectedReport() {
        final List<ProjectDependency> dependencies = this.javaProjectCrawlerRunner.getDependencies().getDependencies();
        final List<ProjectDependency> dependenciesWithFixedLinks = new DependenciesBrokenLinkReplacer(
                this.brokenLinkReplacer).replaceBrokenLinks(dependencies);
        return new DependencyPageRenderer().render(dependenciesWithFixedLinks);
    }

    private List<ValidationFinding> validateFileContent(final String expectedDependenciesPage) {
        try {
            final var actualContent = Files.readString(this.dependenciesFile);
            if (!actualContent.equals(expectedDependenciesPage)) {
                return List.of(ValidationFinding.withMessage(ExaError.messageBuilder("E-PK-53").message(
                        "The dependencies.md file has outdated content.\nExpected content:\n{{expected content|uq}}",
                        expectedDependenciesPage).toString())//
                        .andFix(getFix(expectedDependenciesPage)).build());
            }
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-52")
                    .message("Failed to read dependencies.md for validation.").toString(), exception);
        }
        return Collections.emptyList();
    }

    private ValidationFinding.Fix getFix(final String expectedDependenciesPage) {
        return log -> {
            try {
                Files.writeString(this.dependenciesFile, expectedDependenciesPage, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (final IOException exception) {
                throw new IllegalStateException(
                        ExaError.messageBuilder("E-PK-51").message("Failed to write dependencies.md file.").toString(),
                        exception);
            }
        };
    }
}
