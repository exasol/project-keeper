package com.exasol.projectkeeper.validators.release.github;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

import jakarta.json.*;

/**
 * This class allows executing queries against the GitHub API.
 */
public class GitHubAdapter {
    private static final Logger LOG = Logger.getLogger(GitHubAdapter.class.getName());
    private static final URI GITHUB_API = URI.create("https://api.github.com/");
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final int MAX_ERROR_BODY_LENGTH = 500;
    private final GitHubConnectionProvider gitHubConnectionProvider;
    private final String repoName;
    private final String owner;
    private final HttpClient httpClient;
    private final URI apiBaseUri;

    GitHubAdapter(final GitHubConnectionProvider gitHubConnectionProvider, final String owner, final String repoName) {
        this(gitHubConnectionProvider, owner, repoName, HttpClient.newHttpClient(), GITHUB_API);
    }

    GitHubAdapter(final GitHubConnectionProvider gitHubConnectionProvider, final String owner, final String repoName,
            final HttpClient httpClient, final URI apiBaseUri) {
        this.gitHubConnectionProvider = gitHubConnectionProvider;
        this.owner = owner;
        this.repoName = repoName;
        this.httpClient = httpClient;
        this.apiBaseUri = apiBaseUri;
    }

    /**
     * Connect to a specific GitHub repository.
     * <p>
     * This only supports repositories in GitHub organization {@code exasol}.
     *
     * @param repoName repo name
     * @return a new connected adapter
     */
    public static GitHubAdapter connect(final String repoName) {
        return new GitHubAdapter(new GitHubConnectionProvider(), "exasol", repoName);
    }

    /**
     * Get the state of an issue.
     *
     * @param issueNumber issue number
     * @return state of the issue
     */
    // [impl->dsn~verify-release-mode.verify-issues-closed~1]
    public IssueState getIssueState(final int issueNumber) {
        final String token = gitHubConnectionProvider.getToken().orElseThrow(() -> new IllegalStateException(
                ExaError.messageBuilder("E-PK-CORE-185").message("Failed to get GitHub credentials.")
                        .mitigation("Set environment variable GH_TOKEN or GITHUB_TOKEN")
                        .mitigation("Configure 'gh' command line tool using 'gh auth login'").toString()));
        final IssueState state = fetchIssueState(issueNumber, token);
        LOG.fine(() -> "GitHub issue #" + issueNumber + " has state " + state);
        return state;
    }

    private IssueState fetchIssueState(final int issueNumber, final String token) {
        try {
            final HttpRequest request = buildGetIssueRequest(issueNumber, token);
            final HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            if (response.statusCode() == 404) {
                return IssueState.MISSING;
            }
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IOException(formatErrorResponse(response));
            }
            final JsonObject issue = parseJson(response);
            if ("open".equals(issue.getString("state"))) {
                return IssueState.OPEN;
            } else {
                return IssueState.CLOSED;
            }
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw retrievingIssueStatusFailed(issueNumber, exception);
        } catch (final IOException exception) {
            throw retrievingIssueStatusFailed(issueNumber, exception);
        }
    }

    private UncheckedIOException retrievingIssueStatusFailed(final int issueNumber, final Exception exception) {
        return new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-192")
                .message("Failed to get status of GitHub issue #{{issue number}}: {{cause}}", issueNumber,
                        exception.getMessage())
                .toString(),
                exception instanceof final IOException ioException ? ioException
                        : new IOException(exception));
    }

    private JsonObject parseJson(final HttpResponse<String> response) {
        try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
            return reader.readObject();
        }
    }

    private String formatErrorResponse(final HttpResponse<String> response) {
        final String body = response.body().strip();
        final int detailsLength = Math.min(body.length(), MAX_ERROR_BODY_LENGTH);
        final String details = body.isEmpty() ? "" : ": " + body.substring(0, detailsLength);
        final String truncationMark = body.length() > MAX_ERROR_BODY_LENGTH ? "..." : "";
        return "GitHub API returned HTTP status " + response.statusCode() + details + truncationMark;
    }

    private HttpRequest buildGetIssueRequest(final int issueNumber, final String token) {
        final URI issueUri = apiBaseUri.resolve("repos/" + owner + "/" + repoName + "/issues/" + issueNumber);
        return HttpRequest.newBuilder(issueUri).timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header("User-Agent", "Project-Keeper")
                .GET().build();
    }
}
