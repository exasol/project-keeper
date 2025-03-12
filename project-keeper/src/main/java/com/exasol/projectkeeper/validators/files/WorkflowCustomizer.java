package com.exasol.projectkeeper.validators.files;

/**
 * Interface for customizing a GitHub workflow used by {@link GitHubWorkflowCustomizer}. Implementations of this
 * interface can modify the given workflow as required.
 */
interface WorkflowCustomizer {
    /**
     * Apply the customization to the given workflow.
     * 
     * @param workflow workflow to customize
     */
    void applyCustomization(GitHubWorkflow workflow);
}
