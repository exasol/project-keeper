package com.exasol.projectkeeper.validators.files;

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
        if (environmentName != null) {
            workflow.getJob(jobId).setEnvironment(this.environmentName);
        }
    }
}
