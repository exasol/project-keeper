package com.exasol.projectkeeper.validators.files;

import java.util.List;

import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;

class GitHubWorkflowStepCustomizer implements ContentCustomizingTemplate.ContentCustomizer {
    private final List<StepCustomization> customizations;
    private final GitHubWorkflowIO yaml;
    private final String jobId;

    GitHubWorkflowStepCustomizer(final List<StepCustomization> customizations, final String jobId) {
        this(GitHubWorkflowIO.create(), customizations, jobId);
    }

    GitHubWorkflowStepCustomizer(final GitHubWorkflowIO yaml, final List<StepCustomization> customizations,
            final String jobId) {
        this.yaml = yaml;
        this.customizations = customizations;
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
        for (final StepCustomization customization : customizations) {
            applyCustomization(job, customization);
        }
    }

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
