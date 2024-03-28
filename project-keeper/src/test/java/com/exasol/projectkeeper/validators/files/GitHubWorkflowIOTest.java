package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowIOTest {

    @Test
    void loadWorkflow() {
        final GitHubWorkflow workflow = testee().loadWorkflow("""
                name: CI Build
                on:
                  pull_request:
                jobs:
                  build:
                    runs-on: ubuntu-latest
                    concurrency:
                      group: ${{ github.workflow }}-${{ github.ref }}
                      cancel-in-progress: true
                    outputs:
                      release-required: ${{ steps.check-release.outputs.release-required }}
                    steps:
                      - name: Checkout the repository
                        id: checkout
                        uses: actions/checkout@v4
                        with:
                          fetch-depth: 0
                      - name: Set up JDKs
                        id: setup-jdks
                        uses: actions/setup-java@v4
                        with:
                          distribution: "temurin"
                          java-version: |
                            11
                            17
                          cache: "maven"
                """);
        final Job job = workflow.getJob("build");
        assertAll(() -> assertThat(workflow.getOnTrigger(), hasEntry("pull_request", null)),
                () -> assertThat(job.getRunnerOS(), equalTo("ubuntu-latest")),
                () -> assertThat(job.getConcurrency(), hasEntry("group", "${{ github.workflow }}-${{ github.ref }}")),
                () -> assertThat(job.getConcurrency(), hasEntry("cancel-in-progress", true)),
                () -> assertThat(job.getSteps(), hasSize(2)),
                () -> assertThat(job.getStep("setup-jdks").getWith(), allOf(hasEntry("distribution", "temurin"), //
                        hasEntry("java-version", "11\n17\n"), //
                        hasEntry("cache", "maven"))),
                () -> assertThat(job.getStep("setup-jdks").getUsesAction(), equalTo("actions/setup-java@v4")));
    }

    @Test
    void dumpWorkflow() {
        final String yaml = testee().dumpWorkflow(GitHubWorkflow
                .create(Map.of("jobs", Map.of("build", Map.of("steps", List.of(Map.of("id", "step1")))))));
        assertThat(yaml, equalTo("""
                jobs:
                  build:
                    steps:
                      - {
                        id: step1
                      }
                """));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCases")
    void dumpLoad(final String testDescription, final String inputYaml, final String expectedFormattedYaml) {
        final GitHubWorkflowIO yamlIO = testee();
        final String dumped = yamlIO.dump(yamlIO.load(inputYaml));
        assertThat(testDescription, dumped, equalTo(expectedFormattedYaml));
    }

    static Stream<Arguments> testCases() {
        return Stream.of( //
                testCase("key-value", "key: value", "{\n  key: value\n}\n"), //
                testCase("comments removed", "key: value # comment", "{\n  key: value\n}\n"), //
                testCase("'on' key not converted to true", "on: value", "{\n  on: value\n}\n"), //
                testCase("'on' value not converted to true", "key: on", "{\n  key: on\n}\n"), //
                testCase("multi-line list", """
                        list:
                          - itemA
                          - itemB
                        """, "list: [\n  itemA,\n  itemB\n]\n"),
                testCase("single-line list", "list: [itemA, itemB]", "list: [\n  itemA,\n  itemB\n]\n"), //
                testCase("single-line list with quotes", "list: ['itemA', \"itemB\"]",
                        "list: [\n  itemA,\n  itemB\n]\n"),
                testCaseUnchanged("ci-build step setup JDK", """
                        jobs:
                          build:
                            steps:
                              - name: Set up JDKs
                                uses: actions/setup-java@v4
                                with:
                                  distribution: temurin
                                  java-version: |
                                    11
                                    17
                                  cache: maven
                        """),
                testCase("ci-build step maven verify",
                        """
                                jobs:
                                  build:
                                    steps:
                                      - name: Run tests and build with Maven
                                        run: |
                                          mvn -T 1C --batch-mode clean verify install \
                                              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                                              -DtrimStackTrace=false
                                        env:
                                          GITHUB_TOKEN: ${{ github.token }} # Required for integration tests of GitHub access
                                """,
                        """
                                jobs:
                                  build:
                                    steps:
                                      - name: Run tests and build with Maven
                                        run: |
                                          mvn -T 1C --batch-mode clean verify install \
                                              -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
                                              -DtrimStackTrace=false
                                        env: {
                                          GITHUB_TOKEN: '${{ github.token }}'
                                        }
                                """)

        );
    }

    private GitHubWorkflowIO testee() {
        return GitHubWorkflowIO.create();
    }

    private static Arguments testCaseUnchanged(final String description, final String yaml) {
        return testCase(description, yaml, yaml);
    }

    private static Arguments testCase(final String description, final String inputYaml, final String expectedYaml) {
        return Arguments.of(description, inputYaml, expectedYaml);
    }
}
