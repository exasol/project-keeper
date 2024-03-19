package com.exasol.projectkeeper.validators.release.github;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Issue;

/**
 * This class allows executing queries against the GitHub API.
 */
public class GitHubAdapter {
    private static final Logger LOG = Logger.getLogger(GitHubAdapter.class.getName());
    private final GitHubConnectionProvider gitHubConnectionProvider;
    private final String repoName;
    private final String owner;

    GitHubAdapter(final GitHubConnectionProvider gitHubConnectionProvider, final String owner, final String repoName) {
        this.gitHubConnectionProvider = gitHubConnectionProvider;
        this.owner = owner;
        this.repoName = repoName;
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
        final Issue issue = gitHubConnectionProvider.connect().repos().get(new Coordinates.Simple(owner, repoName))
                .issues().get(issueNumber);
        final Issue.Smart smartIssue = new Issue.Smart(issue);
        try {
            if (!smartIssue.exists()) {
                LOG.fine(() -> "GitHub issue #" + issueNumber + " does not exist.");
                return IssueState.MISSING;
            }
            if (smartIssue.isOpen()) {
                LOG.fine(() -> "GitHub issue #" + issueNumber + " is still open.");
                return IssueState.OPEN;
            } else {
                LOG.fine(() -> "GitHub issue #" + issueNumber + " is closed.");
                return IssueState.CLOSED;
            }
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-192")
                    .message("Failed to get status of GitHub issue #{{issue number}}: {{cause}}", issueNumber,
                            exception.getMessage())
                    .toString(), exception);
        }
    }
}
