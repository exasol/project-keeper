package com.exasol.projectkeeper.validators.files;

import java.util.Optional;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.WorkflowStep;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowStepCustomizer implements ContentCustomizingTemplate.ContentCustomizer {

    private final BuildOptions buildOptions;
    private final GitHubWorkflowIO yaml;
    private final String jobId;

    GitHubWorkflowStepCustomizer(final BuildOptions buildOptions, final String jobId) {
        this(GitHubWorkflowIO.create(), buildOptions, jobId);
    }

    GitHubWorkflowStepCustomizer(final GitHubWorkflowIO yaml, final BuildOptions buildOptions, final String jobId) {
        this.yaml = yaml;
        this.buildOptions = buildOptions;
        this.jobId = jobId;
    }

    @Override
    public String customizeContent(final String content) {
        final GitHubWorkflow workflow = yaml.loadWorkflow(content);
        customizeWorkflow(workflow);
        return yaml.dumpWorkflow(workflow);
    }

    private void customizeWorkflow(final GitHubWorkflow workflow) {

        final Job job = workflow.getJob(jobId);
        job.insertStepsBefore("build-pk-verify", buildOptions.getSetupSteps());
        job.insertStepsAfter("sonar-analysis", buildOptions.getCleanupSteps());

        final Optional<WorkflowStep> customStep = buildOptions.getBuildStep();
        if (customStep.isPresent()) {
            job.replaceStep("build-pk-verify", customStep.get().getRawStep());
        }
    }
}