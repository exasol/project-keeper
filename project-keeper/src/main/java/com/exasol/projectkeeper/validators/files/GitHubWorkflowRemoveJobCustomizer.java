package com.exasol.projectkeeper.validators.files;

import java.util.List;

import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

public class GitHubWorkflowRemoveJobCustomizer implements WorkflowCustomizer {
    private final List<String> jobNamesToRemove;

    GitHubWorkflowRemoveJobCustomizer(final List<String> jobNamesToRemove) {
        this.jobNamesToRemove = jobNamesToRemove;
    }

    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        jobNamesToRemove.forEach(jobName -> removeJob(workflow, jobName));
    }

    private void removeJob(final GitHubWorkflow workflow, final String jobName) {
        workflow.removeJob(jobName);
        if (workflow.hasJob("build")) {
            final Job buildJob = workflow.getJob("build");
            buildJob.getNeeds().remove(jobName);
        }
    }
}
