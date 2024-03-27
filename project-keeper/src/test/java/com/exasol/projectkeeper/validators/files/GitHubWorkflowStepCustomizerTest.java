package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.BuildOptions.Builder;
import com.exasol.projectkeeper.shared.config.WorkflowStep;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class GitHubWorkflowStepCustomizerTest {

    @Test
    void noChangesWithEmptyBuildStepList() {
        final GitHubWorkflow workflow = validate(BuildOptions.builder(), """
                jobs:
                  build:
                    steps:
                """);
        assertThat(workflow.getJob("build").getSteps(), empty());
    }

    @Test
    void noChangesWithSingleBuildSteps() {
        final GitHubWorkflow workflow = validate(BuildOptions.builder(), """
                jobs:
                  build:
                    steps:
                      - name: step1
                        run: echo step1
                """);
        assertThat(workflow.getJob("build").getSteps(), hasSize(1));
    }

    @Test
    void noChangesWithBuild() {
        final GitHubWorkflow workflow = validate(BuildOptions.builder(), """
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: build-pk-verify
                        run: echo step1
                """);
        final Job job = workflow.getJob("build");
        assertAll(() -> assertThat(job.getSteps(), hasSize(1)),
                () -> assertThat(job.getStep("build-pk-verify").getName(), equalTo("step1")));
    }

    @Test
    void replacesBuildStep() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate(BuildOptions.builder().buildStep(customBuildStep), """
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: build-pk-verify
                        run: echo step1
                """);
        final Job job = workflow.getJob("build");
        assertAll(() -> assertThat(job.getSteps(), hasSize(1)),
                () -> assertThat(job.getStep("custom-step").getName(), equalTo("Custom Step")));
    }

    @Test
    void wrongBuildStepId() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final Builder optionsBuilder = BuildOptions.builder().buildStep(customBuildStep);
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> validate(optionsBuilder, """
                        jobs:
                          build:
                            steps:
                              - name: step1
                                id: wrong-id
                                run: echo step1
                        """));
        assertThat(exception.getMessage(), equalTo(
                "No step found for id 'build-pk-verify' in {steps=[{name=step1, id=wrong-id, run=echo step1}]}"));
    }

    @Test
    void insertSetupStep() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate(BuildOptions.builder().setupSteps(List.of(customBuildStep)), """
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: build-pk-verify
                        run: echo step1
                      - name: Sonar
                        id: sonar
                        run: echo sonar
                      - name: Some other cleanup step
                        id: more-cleanup
                        run: echo More cleanup
                """);
        final Job job = workflow.getJob("build");
        final List<Step> steps = job.getSteps();
        assertAll(() -> assertThat(steps, hasSize(4)),
                () -> assertThat(job.getStep("custom-step").getName(), equalTo("Custom Step")),
                () -> assertThat(getStepIds(job), contains("custom-step", "build-pk-verify", "sonar", "more-cleanup")));
    }

    @Test
    void insertCleanupStep() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate(BuildOptions.builder().cleanupSteps(List.of(customBuildStep)), """
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: build-pk-verify
                        run: echo step1
                      - name: Sonar
                        id: sonar
                        run: echo sonar
                      - name: Some other cleanup step
                        id: more-cleanup
                        run: echo More cleanup
                """);
        final Job job = workflow.getJob("build");
        final List<Step> steps = job.getSteps();
        assertAll(() -> assertThat(steps, hasSize(4)),
                () -> assertThat(job.getStep("custom-step").getName(), equalTo("Custom Step")),
                () -> assertThat(getStepIds(job), contains("build-pk-verify", "sonar", "custom-step", "more-cleanup")));
    }

    @Test
    void insertSetupBuildCleanupSteps() {
        final WorkflowStep setupStep1 = WorkflowStep.createStep(Map.of("id", "custom-setup1"));
        final WorkflowStep setupStep2 = WorkflowStep.createStep(Map.of("id", "custom-setup2"));
        final WorkflowStep buildStep = WorkflowStep.createStep(Map.of("id", "custom-build"));
        final WorkflowStep cleanupStep1 = WorkflowStep.createStep(Map.of("id", "custom-cleanup1"));
        final WorkflowStep cleanupStep2 = WorkflowStep.createStep(Map.of("id", "custom-cleanup2"));
        final GitHubWorkflow workflow = validate(BuildOptions.builder().setupSteps(List.of(setupStep1, setupStep2))
                .buildStep(buildStep).cleanupSteps(List.of(cleanupStep1, cleanupStep2)), """
                        jobs:
                          build:
                            steps:
                              - name: step1
                                id: build-pk-verify
                                run: echo step1
                              - name: Sonar
                                id: sonar
                                run: echo sonar
                              - name: Some other cleanup step
                                id: more-cleanup
                                run: echo More cleanup
                        """);
        final Job job = workflow.getJob("build");
        final List<Step> steps = job.getSteps();
        assertAll(() -> assertThat(steps, hasSize(7)), //
                () -> assertThat(getStepIds(job), contains("custom-setup1", "custom-setup2", "custom-build", "sonar",
                        "custom-cleanup1", "custom-cleanup2", "more-cleanup")));
    }

    private List<String> getStepIds(final Job job) {
        return job.getSteps().stream().map(Step::getId).toList();
    }

    private GitHubWorkflow validate(final BuildOptions.Builder optionsBuilder, final String workflowTemplate) {
        final String customizedContent = new GitHubWorkflowStepCustomizer(optionsBuilder.build(), "build")
                .customizeContent(workflowTemplate);
        return GitHubWorkflowIO.create().loadWorkflow(customizedContent);
    }
}
