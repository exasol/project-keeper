package com.exasol.projectkeeper.githuboutput;

import static java.util.stream.Collectors.joining;

import java.nio.file.Path;
import java.util.*;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
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
    private final OutputPublisherFactory publisherFactory;
    private final ChangesFileIO changesFileIO;

    GitHubWorkflowOutputPublisher(final ProjectKeeperConfig config, final Path projectDir, final String projectVersion,
            final OutputPublisherFactory publisherFactory, final ChangesFileIO changesFileIO) {
        this.config = config;
        this.projectDir = projectDir;
        this.projectVersion = projectVersion;
        this.publisherFactory = publisherFactory;
        this.changesFileIO = changesFileIO;
    }

    /**
     * Create a new publisher.
     * 
     * @param config         Project Keeper configuration
     * @param projectDir     project directory
     * @param projectVersion project version
     * @return a new publisher
     */
    public static GitHubWorkflowOutputPublisher create(final ProjectKeeperConfig config, final Path projectDir,
            final String projectVersion) {
        return new GitHubWorkflowOutputPublisher(config, projectDir, projectVersion,
                new OutputPublisherFactory(System.getenv()), new ChangesFileIO());
    }

    /**
     * Publish all values.
     */
    public void publish() {
        final ChangesFile changesFile = readChangesFile();
        try (OutputPublisher publisher = publisherFactory.create()) {
            // [impl->dsn~verify-release-mode.output-parameters.project-version~1]
            publisher.publish("version", projectVersion);
            // [impl->dsn~verify-release-mode.output-parameters.code-name~1]
            publisher.publish("release-title", changesFile.getCodeName());
            // [impl->dsn~verify-release-mode.output-parameters.release-notes~1]
            publisher.publish("release-notes", extractReleaseNotes(changesFile));
            // [impl->dsn~verify-release-mode.output-parameters.release-artifacts~1]
            publisher.publish("release-artifacts", getReleaseArtifacts());
        }
    }

    private String getReleaseArtifacts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getReleaseArtifacts'");
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

    private ChangesFile readChangesFile() {
        return changesFileIO.read(projectDir.resolve(ChangesFile.getPathForVersion(projectVersion)));
    }
}
