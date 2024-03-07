package com.exasol.projectkeeper.githuboutput;

import java.nio.file.Path;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.changesfile.ChangesFileIO;

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

    GitHubWorkflowOutputPublisher(final ProjectKeeperConfig config, final Path projectDir, final String projectVersion,
            final OutputPublisherFactory publisherFactory, final ChangesFileIO changesFileIO) {
        this.config = config;
        this.projectDir = projectDir;
        this.projectVersion = projectVersion;
        this.publisherFactory = publisherFactory;
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
        try (OutputPublisher publisher = publisherFactory.create()) {
            publisher.publish("version", projectVersion);
        }
    }
}
