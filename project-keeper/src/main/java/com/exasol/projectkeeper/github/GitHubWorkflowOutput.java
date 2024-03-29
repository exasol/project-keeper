package com.exasol.projectkeeper.github;

import static com.exasol.projectkeeper.shared.config.SourceType.GOLANG;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.Source;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
import com.exasol.projectkeeper.sources.AnalyzedSource;
import com.exasol.projectkeeper.validators.changesfile.*;

/**
 * This class writes output parameters to the file referenced by environment variable {@code GITHUB_OUTPUT} if it is
 * defined. If the variable is not defined, this will log the output parameters.
 */
// [impl->dsn~verify-modes.output-parameters~1]
public class GitHubWorkflowOutput {

    @SuppressWarnings("unused") // Will be used soon
    private final ProjectKeeperConfig config;
    private final Path projectDir;
    private final String projectVersion;
    private final List<AnalyzedSource> analyzedSources;
    private final OutputPublisherFactory publisherFactory;
    private final ChangesFileIO changesFileIO;

    GitHubWorkflowOutput(final ProjectKeeperConfig config, final Path projectDir, final String projectVersion,
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
    public static GitHubWorkflowOutput create(final ProjectKeeperConfig config, final Path projectDir,
            final String projectVersion, final List<AnalyzedSource> analyzedSources) {
        return new GitHubWorkflowOutput(config, projectDir, projectVersion, analyzedSources,
                new OutputPublisherFactory(System.getenv()), new ChangesFileIO());
    }

    /**
     * Publish all values.
     */
    public void provide() {
        final Optional<ChangesFile> changesFile = readChangesFile();
        try (WorkflowOutput publisher = this.publisherFactory.create()) {
            // [impl->dsn~verify-release-mode.output-parameters.release-tag~1]
            publisher.publish("release-tag", getReleaseTag());
            // [impl->dsn~release-workflow.create-golang-tags~1]
            publisher.publish("additional-release-tags", getAdditionalReleaseTags());
            if (changesFile.isPresent()) {
                // [impl->dsn~verify-release-mode.output-parameters.release-title~1]
                publisher.publish("release-title", this.projectVersion + " " + changesFile.get().getCodeName());
                // [impl->dsn~verify-release-mode.output-parameters.release-notes~1]
                publisher.publish("release-notes", extractReleaseNotes(changesFile.get()));
            }
            // [impl->dsn~verify-release-mode.output-parameters.release-artifacts~1]
            publisher.publish("release-artifacts", getReleaseArtifacts());
        }
    }

    private String getReleaseTag() {
        final String prefix = hasRootGoModule() ? "v" : "";
        return prefix + this.projectVersion;
    }

    private boolean hasRootGoModule() {
        return goModules().anyMatch(Source::isRoot);
    }

    private Stream<Source> goModules() {
        return config.getSources().stream() //
                .filter(source -> source.getType() == GOLANG);
    }

    private String getAdditionalReleaseTags() {
        return goModules().filter(not(Source::isRoot)) //
                .map(Source::getPath) //
                .map(Path::getParent) //
                .map(Path::toString) //
                .map(path -> path + "/v" + this.projectVersion) //
                .collect(joining("\n"));
    }

    private String getReleaseArtifacts() {
        return Stream.of(sourceReleaseArtifacts(), errorCodeReports(), customArtifacts()) //
                .flatMap(Function.identity()) //
                .map(Path::toString).collect(joining("\n"));
    }

    // [impl->dsn~customize-release-artifacts-jar~0]
    private Stream<Path> sourceReleaseArtifacts() {
        return this.analyzedSources.stream() //
                .filter(AnalyzedMavenSource.class::isInstance) //
                .map(AnalyzedMavenSource.class::cast) //
                .filter(source -> source.getReleaseArtifactName() != null) //
                .map(source -> source.getPath().getParent().resolve("target").resolve(source.getReleaseArtifactName()));
    }

    // [impl->dsn~customize-release-artifacts-hard-coded~0]
    private Stream<Path> errorCodeReports() {
        return this.analyzedSources.stream() //
                .filter(AnalyzedMavenSource.class::isInstance) //
                .map(AnalyzedMavenSource.class::cast) //
                .filter(AnalyzedMavenSource::isRootProject) //
                .map(source -> this.projectDir.resolve("target/error_code_report.json"));
    }

    // [impl->dsn~customize-release-artifacts-custom~0]
    private Stream<Path> customArtifacts() {
        return config.getSources().stream().flatMap(this::customArtifacts);
    }

    private Stream<Path> customArtifacts(final Source source) {
        final Path sourcePath = projectDir.resolve(source.getPath()).getParent();
        return source.getReleaseArtifacts().stream() //
                .map(this::replaceVersionPlaceholder) //
                .map(sourcePath::resolve);
    }

    private Path replaceVersionPlaceholder(final Path path) {
        return Path.of(path.toString().replace("${version}", this.projectVersion));
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
        final Path file = this.projectDir.resolve(ChangesFile.getPathForVersion(this.projectVersion));
        if (Files.exists(file)) {
            return Optional.of(this.changesFileIO.read(file));
        } else {
            return Optional.empty();
        }
    }
}
