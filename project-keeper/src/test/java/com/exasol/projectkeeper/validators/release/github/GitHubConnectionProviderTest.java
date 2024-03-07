package com.exasol.projectkeeper.validators.release.github;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class GitHubConnectionProviderTest {

    @Test
    void getTokenFromGhToken() {
        assertThat(testee(Map.of("GH_TOKEN", "gh_token")).getToken().get(), equalTo("gh_token"));
    }

    @Test
    void getTokenFromGithubToken() {
        assertThat(testee(Map.of("GITHUB_TOKEN", "github_token")).getToken().get(), equalTo("github_token"));
    }

    @Test
    void getTokenFromGhTool() {
        assertThat(testee(emptyMap()).getToken(), notNullValue());
    }

    private GitHubConnectionProvider testee(final Map<String, String> environmentVariables) {
        return new GitHubConnectionProvider(environmentVariables);
    }
}
