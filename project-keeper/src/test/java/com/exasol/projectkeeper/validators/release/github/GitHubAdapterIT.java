package com.exasol.projectkeeper.validators.release.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GitHubAdapterIT {

    private static GitHubAdapter adapter;

    @BeforeAll
    static void createAdapter() {
        assumeTrue(new GitHubConnectionProvider().getToken().isPresent(), "GitHub token available");
        adapter = GitHubAdapter.connect("project-keeper");
    }

    // [itest->dsn~verify-release-mode.verify-issues-closed~1]
    @ParameterizedTest
    @CsvSource({ "1, CLOSED", "0, MISSING", "-1, MISSING" })
    void getIssueState(final int issueNumber, final IssueState expectedState) {
        assertThat(adapter.getIssueState(issueNumber), equalTo(expectedState));
    }
}
