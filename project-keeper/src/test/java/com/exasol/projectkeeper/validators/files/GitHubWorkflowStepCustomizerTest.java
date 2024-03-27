package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.WorkflowStep;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

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

    private GitHubWorkflow validate(final BuildOptions.Builder optionsBuilder, final String workflowTemplate) {
        final String customizedContent = new GitHubWorkflowStepCustomizer(optionsBuilder.build(), "build")
                .customizeContent(workflowTemplate);
        return GitHubWorkflowIO.create().loadGitHubWorkflow(customizedContent);
    }
}
