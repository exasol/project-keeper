package com.exasol.projectkeeper.validators.files;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowEnvironmentCustomizer implements GitHubWorkflowCustomizer.WorkflowCustomizer {

    private final String environmentName;
    private final String jobId;

    GitHubWorkflowEnvironmentCustomizer(final String jobId, final String environmentName) {
        this.jobId = jobId;
        this.environmentName = environmentName;
    }

    // [impl->dsn~customize-build-process.ci-build.environment~1]
    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        if (environmentName == null) {
            return;
        }
        final Job job = workflow.getJob(jobId);
        if (job == null) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-207")
                    .message("GitHub Workflow does not have a job with ID {{job id}}", jobId).toString());
        }
        job.setEnvironment(this.environmentName);
    }
}
