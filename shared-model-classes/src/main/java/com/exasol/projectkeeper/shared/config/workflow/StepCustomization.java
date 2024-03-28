package com.exasol.projectkeeper.shared.config.workflow;

import java.util.Objects;

/**
 * This class defines how a GitHub workflow should be modified by inserting or replacing a step.
 */
public final class StepCustomization {

    private final Type type;
    private final String stepId;
    private final WorkflowStep step;

    private StepCustomization(final Builder builder) {
        this.type = Objects.requireNonNull(builder.type, "type");
        this.stepId = Objects.requireNonNull(builder.stepId, "stepId");
        this.step = Objects.requireNonNull(builder.step, "step");
    }

    /**
     * Get the type of customization.
     * 
     * @return customization type
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the step ID.
     * 
     * @return step ID
     */
    public String getStepId() {
        return stepId;
    }

    /**
     * Get the step to insert or replace.
     * 
     * @return steps
     */
    public WorkflowStep getStep() {
        return step;
    }

    /**
     * Create a new builder for {@link StepCustomization} instances.
     * 
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /** Builder class for {@link StepCustomization} instances. */
    public static class Builder {
        private Type type;
        private String stepId;
        private WorkflowStep step;

        private Builder() {
        }

        /**
         * Set the type of customization.
         * 
         * @param type type of customization
         * @return {@code this} for fluent programming
         */
        public Builder type(final Type type) {
            this.type = type;
            return this;
        }

        /**
         * Set the location of the customization, i.e. where the step should be inserted or replaced.
         * 
         * @param stepId step ID
         * @return {@code this} for fluent programming
         */
        public Builder stepId(final String stepId) {
            this.stepId = stepId;
            return this;
        }

        /**
         * Set the step to insert or replace.
         * 
         * @param step step to insert or replace
         * @return {@code this} for fluent programming
         */
        public Builder step(final WorkflowStep step) {
            this.step = step;
            return this;
        }

        /**
         * Build a new {@link StepCustomization} instance.
         * 
         * @return built class
         */
        public StepCustomization build() {
            return new StepCustomization(this);
        }
    }

    @Override
    public String toString() {
        return "StepCustomization [type=" + type + ", stepId=" + stepId + ", step=" + step + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, stepId, step);
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
        final StepCustomization other = (StepCustomization) obj;
        return type == other.type && Objects.equals(stepId, other.stepId) && Objects.equals(step, other.step);
    }

    /**
     * Customization type for a {@link StepCustomization}.
     */
    public enum Type {
        /** Insert the step after the configured step ID. */
        INSERT_AFTER,
        /** Replace the step with the new one. */
        REPLACE
    }
}
