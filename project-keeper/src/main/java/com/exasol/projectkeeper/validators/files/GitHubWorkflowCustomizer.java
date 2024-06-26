package com.exasol.projectkeeper.validators.files;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * This class customizes a GitHub workflow by delegating to a list of {@link WorkflowCustomizer}s.
 */
class GitHubWorkflowCustomizer implements ContentCustomizingTemplate.ContentCustomizer {
    private static final String GENERATED_COMMENT = "# This file was generated by Project Keeper.\n";
    private final GitHubWorkflowIO yaml;
    private final List<WorkflowCustomizer> customizer;

    GitHubWorkflowCustomizer(final WorkflowCustomizer... customizer) {
        this(GitHubWorkflowIO.create(), asList(customizer));
    }

    GitHubWorkflowCustomizer(final GitHubWorkflowIO yaml, final List<WorkflowCustomizer> customizer) {
        this.yaml = yaml;
        this.customizer = customizer;
    }

    @Override
    public String customizeContent(final String content) {
        final GitHubWorkflow workflow = yaml.loadWorkflow(content);
        customizeWorkflow(workflow);
        return GENERATED_COMMENT + yaml.dumpWorkflow(workflow);
    }

    private void customizeWorkflow(final GitHubWorkflow workflow) {
        this.customizer.forEach(c -> c.applyCustomization(workflow));
    }

    /**
     * Interface for customizing a GitHub workflow. Implementations of this interface can modify the given workflow as
     * required.
     */
    interface WorkflowCustomizer {
        /**
         * Apply the customization to the given workflow.
         * 
         * @param workflow workflow to customize
         */
        void applyCustomization(GitHubWorkflow workflow);
    }
}
