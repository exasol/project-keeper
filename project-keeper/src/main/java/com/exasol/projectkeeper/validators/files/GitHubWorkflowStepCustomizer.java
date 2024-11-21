package com.exasol.projectkeeper.validators.files;

import java.util.List;

import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowStepCustomizer implements GitHubWorkflowCustomizer.WorkflowCustomizer {
    private final List<StepCustomization> customizations;

    GitHubWorkflowStepCustomizer(final List<StepCustomization> customizations) {
        this.customizations = customizations;
    }

    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        for (final StepCustomization customization : customizations) {
            final Job job = workflow.getJob(customization.getJobId());
            applyCustomization(job, customization);
        }
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
