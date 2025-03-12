package com.exasol.projectkeeper.validators.files;

import java.util.List;
import java.util.Optional;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.config.ProjectKeeperConfigReader;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowStepCustomizer implements WorkflowCustomizer {
    private final String workflowName;
    private final List<StepCustomization> customizations;

    GitHubWorkflowStepCustomizer(final String workflowName, final List<StepCustomization> customizations) {
        this.workflowName = workflowName;
        this.customizations = customizations;
    }

    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        for (final StepCustomization customization : customizations) {
            final Job job = getJob(workflow, customization);
            applyCustomization(job, customization);
        }
    }

    private Job getJob(final GitHubWorkflow workflow, final StepCustomization customization) {
        final Optional<String> jobId = customization.getJobId();
        if (jobId.isPresent()) {
            return workflow.getJob(jobId.get());
        }
        final List<Job> allJobs = workflow.getJobs();
        if (allJobs.size() == 1) {
            return allJobs.get(0);
        }
        throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-208")
                .message("Missing job in step customization of workflow {{workflow}} in file {{config file}}.",
                        workflowName, ProjectKeeperConfigReader.CONFIG_FILE_NAME)
                .mitigation("Add job with one of the following values: {{available jobs}}.",
                        allJobs.stream().map(Job::getId).toList())
                .toString());
    }

    // [impl->dsn~customize-build-process.insert-step-after~0]
    // [impl->dsn~customize-build-process.replace-step~0]
    private void applyCustomization(final Job job, final StepCustomization customization) {
        switch (customization.getType()) {
            case REPLACE:
                job.replaceStep(customization.getStepId(), customization.getStep());
                break;
            case INSERT_AFTER:
                job.insertStepAfter(customization.getStepId(), customization.getStep());
                break;
            default:
                throw new IllegalStateException("Unknown customization type: " + customization.getType());
        }
    }
}
