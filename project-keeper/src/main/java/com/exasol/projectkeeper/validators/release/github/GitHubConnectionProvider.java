package com.exasol.projectkeeper.validators.release.github;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.github.GitHub;

import com.exasol.errorreporting.ExaError;
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

    GitHub connect() {
        return getToken().map(this::connectWithToken) //
                .or(this::connectWithoutToken) //
                .orElseThrow(() -> new IllegalStateException(
                        ExaError.messageBuilder("E-PK-CORE-185").message("Failed to get GitHub credentials.")
                                .mitigation("Set environment variable GH_TOKEN or GITHUB_TOKEN")
                                .mitigation("Configure 'gh' command line tool using 'gh auth login'").toString()));
    }

    private GitHub connectWithToken(final String token) {
        try {
            return GitHub.connectUsingOAuth(token);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private Optional<GitHub> connectWithoutToken() {
        try {
            return Optional.of(GitHub.connect());
        } catch (final IOException exception) {
            LOG.log(Level.WARNING, exception,
                    () -> "Failed to connect using default credentials: " + exception.getMessage());
            return Optional.empty();
        }
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
            process.waitUntilFinished(Duration.ofSeconds(1));
            return Optional.of(process.getOutputStreamContent().trim());
        } catch (final RuntimeException exception) {
            LOG.log(Level.WARNING, exception,
                    () -> "Failed to get GitHub token from 'gh' command line: " + exception.getMessage());
            return Optional.empty();
        }
    }
}
