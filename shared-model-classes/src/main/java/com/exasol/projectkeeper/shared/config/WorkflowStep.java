package com.exasol.projectkeeper.shared.config;

import java.util.*;

/**
 * A build step in a GitHub workflow.
 */
public final class WorkflowStep {

    private final Map<String, Object> rawStep;

    private WorkflowStep(final Map<String, Object> rawStep) {
        this.rawStep = Objects.requireNonNull(rawStep, "rawStep");
    }

    /**
     * Create a list of workflow steps from a list of raw steps parsed from a YAML file.
     * 
     * @param setupSteps list of raw steps
     * @return list of workflow steps
     */
    public static List<WorkflowStep> createSteps(final List<Map<String, Object>> setupSteps) {
        return setupSteps.stream().map(WorkflowStep::createStep).toList();
    }

    /**
     * Create a workflow step from a raw step parsed from a YAML file.
     * 
     * @param buildStep raw step
     * @return workflow step
     */
    public static WorkflowStep createStep(final Map<String, Object> buildStep) {
        return new WorkflowStep(buildStep);
    }

    /**
     * Get the original raw content of the step.
     * 
     * @return original raw content
     */
    public Map<String, Object> getRawStep() {
        return rawStep;
    }

    @Override
    public String toString() {
        return "WorkflowStep [rawStep=" + rawStep + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawStep);
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
        final WorkflowStep other = (WorkflowStep) obj;
        return Objects.equals(rawStep, other.rawStep);
    }
}
