package com.exasol.projectkeeper.shared.config.workflow;

import static java.util.Collections.emptyList;

import java.util.*;

/**
 * This configuration class allows customizing a GitHub workflow.
 */
public final class CustomWorkflow {

    private final String workflowName;
    private final List<StepCustomization> steps;

    private CustomWorkflow(final Builder builder) {
        this.workflowName = Objects.requireNonNull(builder.workflowName, "workflowName");
        this.steps = builder.steps == null ? emptyList() : builder.steps;
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
    public List<StepCustomization> getSteps() {
        return steps;
    }

    /**
     * Create a new builder for the {@link CustomWorkflow}.
     * 
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "WorkflowOptions [workflowName=" + workflowName + ", customizations=" + steps + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowName, steps);
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
        final CustomWorkflow other = (CustomWorkflow) obj;
        return Objects.equals(workflowName, other.workflowName) && Objects.equals(steps, other.steps);
    }

    /**
     * Builder class for the BuildWorkflow.
     */
    public static class Builder {
        private String workflowName;
        private List<StepCustomization> steps = null;

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
         * @param step step customization
         * @return the builder instance
         */
        public Builder addStep(final StepCustomization step) {
            if (steps == null) {
                steps = new ArrayList<>();
            }
            this.steps.add(Objects.requireNonNull(step, "step"));
            return this;
        }

        /**
         * Set step customization.
         *
         * @param steps step customizations
         * @return the builder instance
         */
        public Builder steps(final List<StepCustomization> steps) {
            this.steps = Objects.requireNonNull(steps, "steps");
            return this;
        }

        /**
         * Build the BuildWorkflow instance.
         *
         * @return the BuildWorkflow instance
         */
        public CustomWorkflow build() {
            return new CustomWorkflow(this);
        }
    }
}
