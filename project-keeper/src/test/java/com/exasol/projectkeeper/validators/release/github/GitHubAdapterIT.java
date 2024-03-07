package com.exasol.projectkeeper.validators.release.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GitHubAdapterIT {

    private static GitHubAdapter adapter;

    @BeforeAll
    static void createAdapter() {
        assumeTrue(new GitHubConnectionProvider().getToken().isPresent(), "GitHub token available");
        adapter = GitHubAdapter.connect("project-keeper");
    }

    @Test
    void getClosedIssues() {
        final Set<Integer> closedIssues = adapter.getClosedIssues();
        assertAll(() -> assertThat(closedIssues, hasSize(allOf(greaterThan(520), lessThan(10000000)))),
                () -> assertThat(closedIssues, allOf(hasItem(1), hasItem(2), hasItem(3))));
    }
}
