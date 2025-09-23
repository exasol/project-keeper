package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.workflow.WorkflowStep;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class GitHubWorkflowTest {

    @Test
    void getJob() {
        final GitHubWorkflow workflow = read("""
                name: CI Build
                jobs:
                  build:
                    steps:
                """);
        assertThat(workflow.getJob("build"), notNullValue());
    }

    @Test
    void getJobMissing() {
        final GitHubWorkflow workflow = read("""
                name: CI Build
                jobs:
                  build:
                    steps:
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workflow.getJob("missing-job"));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-207: GitHub Workflow does not have a job with ID 'missing-job'. Choose one of ['build']."));
    }

    @Test
    void getOnTrigger() {
        final GitHubWorkflow workflow = read("""
                name: CI Build
                on:
                  push:
                    branches: [main]
                """);
        assertThat(workflow.getOnTrigger(), equalTo(Map.of("push", Map.of("branches", List.of("main")))));
    }

    @Test
    void getRawObject() {
        final GitHubWorkflow workflow = read("""
                name: CI Build
                """);
        assertThat(workflow.getRawObject(), equalTo(Map.of("name", "CI Build")));
    }

    @Test
    void getJobConcurrency() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    concurrency:
                      group: '${{ github.workflow }}-${{ github.ref }}'
                      cancel-in-progress: true
                """);
        assertThat(workflow.getJob("build").getConcurrency(),
                equalTo(Map.of("group", "${{ github.workflow }}-${{ github.ref }}", "cancel-in-progress", true)));
    }

    @Test
    void getJobEnv() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    env:
                      DEFAULT_EXASOL_DB_VERSION: v1
                """);
        assertThat(workflow.getJob("build").getEnv(), equalTo(Map.of("DEFAULT_EXASOL_DB_VERSION", "v1")));
    }

    @Test
    void getJobRunnerOs() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    runs-on: runner-os
                """);
        assertThat(workflow.getJob("build").getRunnerOS(), equalTo("runner-os"));
    }

    @Test
    void getJobStrategy() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    strategy:
                      fail-fast: false
                      matrix:
                        exasol_db_version: [v1, v2]
                """);
        assertThat(workflow.getJob("build").getStrategy(),
                equalTo(Map.of("fail-fast", false, "matrix", Map.of("exasol_db_version", List.of("v1", "v2")))));
    }

    @Test
    void getJobEmptyStepList() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                """);
        assertThat(workflow.getJob("build").getSteps(), empty());
    }

    @Test
    void getJobStepList() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - name: step1
                      - name: step2
                """);
        assertThat(workflow.getJob("build").getSteps(), hasSize(2));
    }

    @Test
    void getJobMissingStepFails() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                """);
        final Job job = workflow.getJob("build");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> job.getStep("missing"));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-206: Job has no step with ID 'missing': {steps=[{id=step1}]}"));
    }

    @Test
    void getJobMissingStepIdFails() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - name: Missing Step ID
                """);
        final Job job = workflow.getJob("build");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> job.getStep("missing"));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-204: Step has no field 'id': {name=Missing Step ID}"));
    }

    @Test
    void getJobStepId() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getId(), equalTo("step1"));
    }

    @Test
    void getJobStepName() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        name: Step 1
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getName(), equalTo("Step 1"));
    }

    @Test
    void getJobStepNameMissing() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, step::getName);
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-204: Step has no field 'name': {id=step1}"));
    }

    @Test
    void getJobStepIfCondition() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        if: ${{ condition }}
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getIfCondition(), equalTo("${{ condition }}"));
    }

    @Test
    void getJobStepRunCommand() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        run: echo step
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getRunCommand(), equalTo("echo step"));
    }

    @Test
    void getJobStepRunCommandMultiline() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        run: |
                          echo step 1
                          echo step 2
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getRunCommand(), equalTo("echo step 1\necho step 2\n"));
    }

    @Test
    void getJobStepRunUsesAction() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        uses: actions/setup-go@v6
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getUsesAction(), equalTo("actions/setup-go@v6"));
    }

    @Test
    void getJobStepWith() {
        final GitHubWorkflow workflow = read("""
                jobs:
                  build:
                    steps:
                      - id: step1
                        uses: actions/setup-go@v6
                        with:
                          go-version: '1.24'
                          cache-dependency-path: .project-keeper.yml
                """);
        final Step step = workflow.getJob("build").getStep("step1");
        assertThat(step.getWith(),
                equalTo(Map.of("go-version", "1.24", "cache-dependency-path", ".project-keeper.yml")));
    }

    @Test
    void replaceStep() {
        final Job job = read("""
                jobs:
                  build:
                    steps:
                      - id: step-to-replace
                        name: Old Step
                """).getJob("build");
        job.replaceStep("step-to-replace", WorkflowStep.createStep(Map.of("id", "new-step", "name", "New Step")));
        assertAll(() -> assertThat(job.getSteps(), hasSize(1)),
                () -> assertThat(job.getStep("new-step").getName(), equalTo("New Step")));
    }

    @Test
    void replaceStepMissing() {
        final Job job = read("""
                jobs:
                  build:
                    steps:
                      - id: step-to-replace
                        name: Old Step
                """).getJob("build");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> job.replaceStep("missing", null));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-205: No step found for id 'missing' in ['step-to-replace']"));
    }

    @Test
    void insertStep() {
        final Job job = read("""
                jobs:
                  build:
                    steps:
                      - id: step0
                        name: Step 0
                """).getJob("build");
        job.insertStepAfter("step0", WorkflowStep.createStep(Map.of("id", "new-step", "name", "New Step")));
        assertAll(() -> assertThat(job.getSteps(), hasSize(2)),
                () -> assertThat(job.getStep("step0").getName(), equalTo("Step 0")),
                () -> assertThat(job.getStep("new-step").getName(), equalTo("New Step")));
    }

    @Test
    void insertStepMissing() {
        final Job job = read("""
                jobs:
                  build:
                    steps:
                      - id: step-to-replace
                        name: Old Step
                """).getJob("build");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> job.insertStepAfter("missing", null));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-205: No step found for id 'missing' in ['step-to-replace']"));
    }

    @Test
    void setEnvironment() {
        final Job job = read("""
                jobs:
                  build:
                    steps:
                """).getJob("build");
        job.setEnvironment("my-env");
        assertThat(job.getEnvironment(), equalTo("my-env"));
    }

    private GitHubWorkflow read(final String yaml) {
        return GitHubWorkflowIO.create().loadWorkflow(yaml);
    }
}
