package com.exasol.projectkeeper.validators.release.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
        assertThat(adapter.getClosedIssues(), hasSize(allOf(greaterThan(520), lessThan(10000000))));
    }
}
