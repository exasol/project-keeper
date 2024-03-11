package com.exasol.projectkeeper.githuboutput;

import static java.util.stream.Collectors.joining;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.*;

/**
 * This class writes output parameters to the file referenced by environment variable {@code GITHUB_OUTPUT} if it is
 * defined. If the variable is not defined, this will log the output parameters.
 */
// [impl->dsn~verify-modes.output-parameters~1]
public class GitHubWorkflowOutputPublisher {

    private final ProjectKeeperConfig config;
    private final Path projectDir;
    private final String projectVersion;
    private final List<AnalyzedSource> analyzedSources;
    private final OutputPublisherFactory publisherFactory;
    private final ChangesFileIO changesFileIO;

    GitHubWorkflowOutputPublisher(final ProjectKeeperConfig config, final Path projectDir, final String projectVersion,
            final List<AnalyzedSource> analyzedSources, final OutputPublisherFactory publisherFactory,
            final ChangesFileIO changesFileIO) {
        this.config = config;
        this.projectDir = projectDir;
        this.projectVersion = projectVersion;
        this.analyzedSources = analyzedSources;
        this.publisherFactory = publisherFactory;
        this.changesFileIO = changesFileIO;
    }

    /**
     * Create a new publisher.
     * 
     * @param config          Project Keeper configuration
     * @param projectDir      project directory
     * @param projectVersion  project version
     * @param analyzedSources analyzed sources
     * @return a new publisher
     */
    public static GitHubWorkflowOutputPublisher create(final ProjectKeeperConfig config, final Path projectDir,
            final String projectVersion, final List<AnalyzedSource> analyzedSources) {
        return new GitHubWorkflowOutputPublisher(config, projectDir, projectVersion, analyzedSources,
                new OutputPublisherFactory(System.getenv()), new ChangesFileIO());
    }

    /**
     * Publish all values.
     */
    public void publish() {
        final Optional<ChangesFile> changesFile = readChangesFile();
        try (WorkflowOutput publisher = publisherFactory.create()) {
            // [impl->dsn~verify-release-mode.output-parameters.project-version~1]
            publisher.publish("version", projectVersion);
            if (changesFile.isPresent()) {
                // [impl->dsn~verify-release-mode.output-parameters.code-name~1]
                publisher.publish("release-title", changesFile.get().getCodeName());
                // [impl->dsn~verify-release-mode.output-parameters.release-notes~1]
                publisher.publish("release-notes", extractReleaseNotes(changesFile.get()));
            }
            // [impl->dsn~verify-release-mode.output-parameters.release-artifacts~1]
            publisher.publish("release-artifacts", getReleaseArtifacts());
        }
    }

    private String getReleaseArtifacts() {
        return Stream.of(sourceReleaseArtifacts(), errorCodeReports()) //
                .flatMap(Function.identity()) //
                .map(Path::toString).collect(joining("\n"));
    }

    // [impl->dsn~customize-release-artifacts-jar~0]
    private Stream<Path> sourceReleaseArtifacts() {
        return analyzedSources.stream() //
                .filter(AnalyzedMavenSource.class::isInstance) //
                .map(AnalyzedMavenSource.class::cast) //
                .filter(source -> source.getReleaseArtifactName() != null) //
                .map(source -> source.getPath().getParent().resolve("target").resolve(source.getReleaseArtifactName()));
    }

    // [impl->dsn~customize-release-artifacts-hard-coded~0]
    private Stream<Path> errorCodeReports() {
        return analyzedSources.stream() //
                .filter(AnalyzedMavenSource.class::isInstance) //
                .map(AnalyzedMavenSource.class::cast) //
                .filter(AnalyzedMavenSource::isRootProject) //
                .map(source -> projectDir.resolve("target/error_code_report.json"));
    }

    private String extractReleaseNotes(final ChangesFile changesFile) {
        final List<String> lines = new ArrayList<>();
        lines.addAll(
                changesFile.getSummarySection().map(ChangesFileSection::getContent).orElseGet(Collections::emptyList));
        for (final ChangesFileSection section : changesFile.getSections()) {
            append(lines, section);
        }
        final Optional<ChangesFileSection> dependencyChangeSection = changesFile.getDependencyChangeSection();
        if (dependencyChangeSection.isPresent()) {
            append(lines, dependencyChangeSection.get());
        }
        return lines.stream().collect(joining("\n"));
    }

    private void append(final List<String> lines, final ChangesFileSection section) {
        lines.add(section.getHeading());
        lines.addAll(section.getContent());
    }

    private Optional<ChangesFile> readChangesFile() {
        final Path file = projectDir.resolve(ChangesFile.getPathForVersion(projectVersion));
        if (Files.exists(file)) {
            return Optional.of(changesFileIO.read(file));
        } else {
            return Optional.empty();
        }
    }
}
