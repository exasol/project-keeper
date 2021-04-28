package com.exasol.projectkeeper.validators.dependencies;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.*;
import com.exasol.projectkeeper.pom.MavenArtifactModelReader;
import com.exasol.projectkeeper.pom.MavenFileModelReader;
import com.exasol.projectkeeper.validators.dependencies.renderer.DependencyPageRenderer;

/**
 * {@link Validator} for the dependencies.md file.
 */
public class DependenciesValidator implements Validator {
    private final File pomFile;
    private final ProjectDependencyReader projectDependencyReader;
    private final Path dependenciesFile;
    private final BrokenLinkReplacer brokenLinkReplacer;

    /**
     * Create a new instance of {@link DependenciesValidator}.
     * 
     * @param fileModelReader     pom file parser
     * @param artifactModelReader maven dependency reader
     * @param pomFile             pom file to validate
     * @param projectDirectory    project root directory
     * @param brokenLinkReplacer  dependency injection for broken link replacer
     */
    public DependenciesValidator(final MavenFileModelReader fileModelReader,
            final MavenArtifactModelReader artifactModelReader, final File pomFile, final Path projectDirectory,
            final BrokenLinkReplacer brokenLinkReplacer) {
        this.brokenLinkReplacer = brokenLinkReplacer;
        this.projectDependencyReader = new ProjectDependencyReader(fileModelReader, artifactModelReader);
        this.pomFile = pomFile;
        this.dependenciesFile = projectDirectory.resolve("dependencies.md");
    }

    @Override
    public List<ValidationFinding> validate() {
        final String expectedDependenciesPage = generateExpectedReport();
        if (!this.dependenciesFile.toFile().exists()) {
            return List
                    .of(new ValidationFinding(
                            ExaError.messageBuilder("E-PK-50")
                                    .message("This project does not have a dependencies.md file.").toString(),
                            getFix(expectedDependenciesPage)));
        } else {
            return validateFileContent(expectedDependenciesPage);
        }
    }

    private String generateExpectedReport() {
        final List<ProjectDependency> dependencies = this.projectDependencyReader.readDependencies(this.pomFile);
        final List<ProjectDependency> dependenciesWithFixedLinks = new DependenciesBrokenLinkReplacer(
                this.brokenLinkReplacer).replaceBrokenLinks(dependencies);
        return new DependencyPageRenderer().render(dependenciesWithFixedLinks);
    }

    private List<ValidationFinding> validateFileContent(final String expectedDependenciesPage) {
        try {
            final var actualContent = Files.readString(this.dependenciesFile);
            if (!actualContent.equals(expectedDependenciesPage)) {
                return List.of(new ValidationFinding(ExaError.messageBuilder("E-PK-53").message(
                        "The dependencies.md file has a outdated content.\nExpected content:\n{{expected content|uq}}",
                        expectedDependenciesPage).toString(), getFix(expectedDependenciesPage)));
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
