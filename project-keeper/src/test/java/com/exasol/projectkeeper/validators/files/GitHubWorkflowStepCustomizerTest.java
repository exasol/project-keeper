package com.exasol.projectkeeper.validators.files;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization.Type;
import com.exasol.projectkeeper.shared.config.workflow.WorkflowStep;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class GitHubWorkflowStepCustomizerTest {

    @Test
    void noChangesWithEmptyBuildStepList() {
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                """);
        assertThat(workflow.getJob("build").getSteps(), empty());
    }

    @Test
    void noChangesWithSingleBuildSteps() {
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: step1
                        run: echo step1
                """);
        assertThat(workflow.getJob("build").getSteps(), hasSize(1));
    }

    @Test
    void missingStepId() {
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: step1
                        run: echo step1
                """);
        final Job job = workflow.getJob("build");
        assertAll(() -> assertThat(job.getSteps(), hasSize(1)),
                () -> assertThat(job.getSteps().get(0).getName(), equalTo("step1")));
    }

    @Test
    void noChangesWithBuild() {
        final GitHubWorkflow workflow = validate("""
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

    // [utest->dsn~customize-build-process.replace-step~0]
    @Test
    void replaceStep() {
        final WorkflowStep newStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: step0
                        id: step0
                      - name: step1
                        id: replaced-step
                      - name: step2
                        id: step2
                """, StepCustomization.builder().type(Type.REPLACE).stepId("replaced-step").step(newStep).build());
        assertThat(getStepIds(workflow.getJob("build")), contains("step0", "custom-step", "step2"));
    }

    @Test
    void replaceStepOtherStepHasMissingId() {
        final WorkflowStep newStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: step0
                        id: step0
                      - name: step1
                        id: replaced-step
                      - name: step2
                """, StepCustomization.builder().type(Type.REPLACE).stepId("replaced-step").step(newStep).build());
        assertThat(getStepNames(workflow.getJob("build")), contains("step0", "Custom Step", "step2"));
    }

    @Test
    void replaceStepWrongBuildStepId() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final StepCustomization customization = StepCustomization.builder().type(Type.REPLACE).stepId("missing-step")
                .step(customBuildStep).build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validate("""
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: wrong-id
                """, customization));
        assertThat(exception.getMessage(),
                equalTo("No step found for id 'missing-step' in {steps=[{name=step1, id=wrong-id}]}"));
    }

    @Test
    void insertStepWrongBuildStepId() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final StepCustomization customization = StepCustomization.builder().type(Type.INSERT_AFTER)
                .stepId("missing-step").step(customBuildStep).build();
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> validate("""
                jobs:
                  build:
                    steps:
                      - name: step1
                        id: wrong-id
                """, customization));
        assertThat(exception.getMessage(),
                equalTo("No step found for id 'missing-step' in {steps=[{name=step1, id=wrong-id}]}"));
    }

    // [utest->dsn~customize-build-process.insert-step-after~0]
    @Test
    void insertStepInTheMiddle() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: Step 0
                        id: step0
                      - name: Step 1
                        id: step1
                """, StepCustomization.builder().type(Type.INSERT_AFTER).stepId("step0").step(customBuildStep).build());
        assertThat(getStepIds(workflow.getJob("build")), contains("step0", "custom-step", "step1"));
    }

    @Test
    void insertStepAtTheEnd() {
        final WorkflowStep customBuildStep = WorkflowStep
                .createStep(Map.of("name", "Custom Step", "id", "custom-step", "run", "echo custom-step"));
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: Step 0
                        id: step0
                      - name: Step 1
                        id: step1
                """, StepCustomization.builder().type(Type.INSERT_AFTER).stepId("step1").step(customBuildStep).build());
        assertThat(getStepIds(workflow.getJob("build")), contains("step0", "step1", "custom-step"));
    }

    @Test
    void insertStepAfterReplacedStep() {
        final WorkflowStep replacedStep = WorkflowStep.createStep(Map.of("id", "replaced-step"));
        final WorkflowStep insertedStep = WorkflowStep.createStep(Map.of("id", "inserted-step"));
        final GitHubWorkflow workflow = validate("""
                jobs:
                  build:
                    steps:
                      - name: Step 0
                        id: step0
                      - name: Step 1
                        id: step1
                """, StepCustomization.builder().type(Type.REPLACE).stepId("step0").step(replacedStep).build(),
                StepCustomization.builder().type(Type.INSERT_AFTER).stepId("replaced-step").step(insertedStep).build());
        assertThat(getStepIds(workflow.getJob("build")), contains("replaced-step", "inserted-step", "step1"));
    }

    private List<String> getStepIds(final Job job) {
        return job.getSteps().stream().map(Step::getId).toList();
    }

    private List<String> getStepNames(final Job job) {
        return job.getSteps().stream().map(Step::getName).toList();
    }

    private GitHubWorkflow validate(final String workflowTemplate, final StepCustomization... customizations) {
        final String customizedContent = new GitHubWorkflowStepCustomizer(asList(customizations), "build")
                .customizeContent(workflowTemplate);
        return GitHubWorkflowIO.create().loadWorkflow(customizedContent);
    }
}
