package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

class GitHubWorkflowJavaVersionCustomizerTest {
    @Test
    void singleJavaVersion() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v4
                        with:
                          distribution: "temurin"
                          java-version: |
                            old
                            version
                          cache: "maven"
                """, List.of("v1"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void updateNewerSetupJavaAction() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                        with:
                          distribution: "temurin"
                          java-version: old
                          cache: "maven"
                """, List.of("v1"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void updateWhenWithIsEmpty() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v4
                        with:
                """, List.of("v1"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void multipleJavaVersion() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v4
                        with:
                          distribution: "temurin"
                          java-version: |
                            old
                            version
                          cache: "maven"
                """, List.of("11", "17", "21"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("11\n17\n21"));
    }

    @Test
    void multipleUnknownAction() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/unknown@v4
                        with:
                          java-version: old version
                """, List.of("11", "17", "21"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"),
                equalTo("old version"));
    }

    @Test
    void ignoreRunStep() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        run: echo Hello
                """, List.of("11", "17", "21"));
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().size(), is(0));
    }

    GitHubWorkflow customize(final String yaml, final List<String> javaVersions) {
        final GitHubWorkflow workflow = GitHubWorkflowIO.create().loadWorkflow(yaml);
        new GitHubWorkflowJavaVersionCustomizer(javaVersions).applyCustomization(workflow);
        return workflow;
    }
}
