package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

class GitHubWorkflowJavaVersionCustomizerTest {
    private static final String LATEST_JAVA_VERSION = "24";

    @Test
    void testUpdateWithActionVersion4() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                        with:
                          distribution: "temurin"
                          java-version: |
                            old
                            version
                          cache: "maven"
                """, List.of("v1"), "v2");
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void testUpdateWithActionVersion5() {
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
                """, List.of("v1"), "v2");
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void testUpdateEmptyJavaVersion() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                        with:
                """, List.of("v1"), "v2");
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void testUpdateAnyJobs() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  any-job-name:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                """, List.of("v1"), "v2");
        assertThat(workflow.getJob("any-job-name").getStep("setup-java").getWith().get("java-version"), equalTo("v1"));
    }

    @Test
    void testUpdateToMultipleJavaVersions() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                        with:
                          distribution: "temurin"
                          java-version: |
                            old
                            version
                          cache: "maven"
                """, List.of("11", "17", "21"), LATEST_JAVA_VERSION);
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"), equalTo("11\n17\n21"));
    }

    @Test
    void testUpdateNextJavaBuild() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  next-java-compatibility:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/setup-java@v5
                        with:
                          distribution: "temurin"
                          java-version: |
                            old
                            version
                          cache: "maven"
                """, List.of("11", "17", "21"), LATEST_JAVA_VERSION);
        assertThat(workflow.getJob("next-java-compatibility").getStep("setup-java").getWith().get("java-version"),
                equalTo(LATEST_JAVA_VERSION));
    }

    @Test
    void testNoUpdateOfUnknownAction() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        uses: actions/unknown@v4
                        with:
                          java-version: old version
                """, List.of("11", "17", "21"), LATEST_JAVA_VERSION);
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().get("java-version"),
                equalTo("old version"));
    }

    @Test
    void ignoreStepWithoutUsesDirective() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                      - name: Set up JDKs
                        id: setup-java
                        run: echo Hello
                """, List.of("11", "17", "21"), LATEST_JAVA_VERSION);
        assertThat(workflow.getJob("build").getStep("setup-java").getWith().size(), is(0));
    }

    GitHubWorkflow customize(final String yaml, final List<String> javaVersions, final String nextJavaVersion) {
        final GitHubWorkflow workflow = GitHubWorkflowIO.create().loadWorkflow(yaml);
        new GitHubWorkflowJavaVersionCustomizer(javaVersions, () -> nextJavaVersion).applyCustomization(workflow);
        return workflow;
    }
}
