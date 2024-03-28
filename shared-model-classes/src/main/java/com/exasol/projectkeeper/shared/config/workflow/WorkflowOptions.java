package com.exasol.projectkeeper.shared.config.workflow;

import java.util.*;

/**
 * This configuration class allows customizing a GitHub workflow.
 */
public final class WorkflowOptions {

    private final String workflowName;
    private final List<StepCustomization> customizations;

    private WorkflowOptions(final Builder builder) {
        this.workflowName = Objects.requireNonNull(builder.workflowName, "workflowName");
        this.customizations = Objects.requireNonNull(builder.customizations, "customizations");
    }

    /**
     * Get the name of the workflow to customize, e.g. {@code ci-build.yml}.
     * 
     * @return workflow name
     */
    public String getWorkflowName() {
        return workflowName;
    }

    /**
     * Get the list of step customizations.
     *
     * @return the list of step customizations
     */
    public List<StepCustomization> getCustomizations() {
        return customizations;
    }

    /**
     * Create a new builder for the {@link WorkflowOptions}.
     * 
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "WorkflowOptions [workflowName=" + workflowName + ", customizations=" + customizations + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowName, customizations);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WorkflowOptions other = (WorkflowOptions) obj;
        return Objects.equals(workflowName, other.workflowName) && Objects.equals(customizations, other.customizations);
    }

    /**
     * Builder class for the BuildWorkflow.
     */
    public static class Builder {
        private String workflowName;
        private List<StepCustomization> customizations = null;

        private Builder() {
        }

        /**
         * Set the name of the workflow to customize, e.g. {@code ci-build.yml}.
         *
         * @param workflowName the name of the workflow
         * @return the builder instance
         */
        public Builder workflowName(final String workflowName) {
            this.workflowName = Objects.requireNonNull(workflowName, "workflowName");
            return this;
        }

        /**
         * Add step customization.
         *
         * @param customization step customization
         * @return the builder instance
         */
        public Builder addCustomization(final StepCustomization customization) {
            if (customizations == null) {
                customizations = new ArrayList<>();
            }
            this.customizations.add(Objects.requireNonNull(customization, "customization"));
            return this;
        }

        /**
         * Set step customization.
         *
         * @param customizations step customizations
         * @return the builder instance
         */
        public Builder customizations(final List<StepCustomization> customizations) {
            this.customizations = Objects.requireNonNull(customizations, "customizations");
            return this;
        }

        /**
         * Build the BuildWorkflow instance.
         *
         * @return the BuildWorkflow instance
         */
        public WorkflowOptions build() {
            return new WorkflowOptions(this);
        }
    }
}
