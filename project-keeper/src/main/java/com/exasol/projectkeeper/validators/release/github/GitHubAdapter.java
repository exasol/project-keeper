package com.exasol.projectkeeper.validators.release.github;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Set;

import org.kohsuke.github.*;

/**
 * This class allows executing queries against the GitHub API.
 */
public class GitHubAdapter {
    private static final String GITHUB_ORG = "exasol";
    private final GitHub gitHub;
    private final String repoName;

    GitHubAdapter(final GitHub gitHub, final String repoName) {
        this.gitHub = gitHub;
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
        final GitHub adapter = new GitHubConnectionProvider().connect();
        return new GitHubAdapter(adapter, GITHUB_ORG + "/" + repoName);
    }

    /**
     * Get the {@link Set} of closed issues for the given repository.
     *
     * @return issue numbers
     */
    public Set<Integer> getClosedIssues() {
        try {
            return gitHub.getRepository(repoName) //
                    .getIssues(GHIssueState.CLOSED).stream() //
                    .map(GHIssue::getNumber) //
                    .collect(toSet());
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
