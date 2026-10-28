package com.exasol.projectkeeper.validators.release.github;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.projectkeeper.sources.analyze.generic.SimpleProcess;

class GitHubConnectionProvider {
    private static final Logger LOG = Logger.getLogger(GitHubConnectionProvider.class.getName());

    private final Map<String, String> environmentVariables;

    GitHubConnectionProvider() {
        this(System.getenv());
    }

    GitHubConnectionProvider(final Map<String, String> environmentVariables) {
        this.environmentVariables = environmentVariables;
    }

    Optional<String> getToken() {
        return getEnv("GH_TOKEN") //
                .or(() -> getEnv("GITHUB_TOKEN")) //
                .or(this::getTokenFromGhTool);
    }

    private Optional<String> getEnv(final String name) {
        return Optional.ofNullable(environmentVariables.get(name));
    }

    private Optional<String> getTokenFromGhTool() {
        try {
            final SimpleProcess process = SimpleProcess.start(List.of("gh", "auth", "token"));
            process.waitUntilFinished(Duration.ofSeconds(10));
            return Optional.of(process.getOutputStreamContent().trim());
        } catch (final RuntimeException exception) {
            LOG.log(Level.WARNING, exception,
                    () -> "Failed to get GitHub token from 'gh' command line: " + exception.getMessage());
            return Optional.empty();
        }
    }
}
