package com.exasol.projectkeeper.validators.files;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import com.exasol.projectkeeper.shared.config.workflow.CustomJob;
import com.exasol.projectkeeper.shared.config.workflow.JobPermissions;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowJobPermissionsCustomizer implements GitHubWorkflowCustomizer.WorkflowCustomizer {

    private final Map<String, JobPermissions> permissions;

    GitHubWorkflowJobPermissionsCustomizer(final List<CustomJob> jobs) {
        this.permissions = jobs.stream().collect(toMap(CustomJob::getJobName, CustomJob::getPermissions));
    }

    // [impl->dsn~customize-build-process.ci-build.environment~1]
    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        workflow.getJobs().forEach(this::applyCustomization);
    }

    private void applyCustomization(final Job job) {
        final JobPermissions jobPermissions = permissions.get(job.getId());
        if (jobPermissions != null) {
            job.setPermissions(jobPermissions);
        }
    }
}
