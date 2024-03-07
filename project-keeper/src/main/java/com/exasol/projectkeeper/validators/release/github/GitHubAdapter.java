package com.exasol.projectkeeper.validators.release.github;

import static java.util.stream.Collectors.toSet;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Issue;
import com.jcabi.github.Issues.Qualifier;
import com.jcabi.github.Issues.Sort;
import com.jcabi.github.Search.Order;

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
     * Get the {@link Set} of closed issues for the given repository.
     *
     * @return issue numbers
     */
    public Set<Integer> getClosedIssues() {
        LOG.fine(() -> "Fetching closed issues for repo " + owner + "/" + repoName + "...");
        final Instant start = Instant.now();
        final Set<Integer> issues = StreamSupport.stream(gitHubConnectionProvider.connect() //
                .repos().get(new Coordinates.Simple(owner, repoName)) //
                .issues().search(Sort.CREATED, Order.ASC, stateClosed()) //
                .spliterator(), false) //
                .map(Issue::number) //
                .collect(toSet());
        LOG.fine(() -> "Found " + issues.size() + " issues for repo " + owner + "/" + repoName + " in "
                + Duration.between(start, Instant.now()));
        return issues;
    }

    private EnumMap<Qualifier, String> stateClosed() {
        final EnumMap<Qualifier, String> qualifiers = new EnumMap<>(Qualifier.class);
        qualifiers.put(Qualifier.STATE, "closed");
        return qualifiers;
    }
}
