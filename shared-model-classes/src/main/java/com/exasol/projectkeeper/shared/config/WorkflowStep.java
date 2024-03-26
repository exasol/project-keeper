package com.exasol.projectkeeper.shared.config;

import java.util.*;

public final class WorkflowStep {

    private final Map<String, Object> rawStep;

    private WorkflowStep(final Map<String, Object> rawStep) {
        this.rawStep = Objects.requireNonNull(rawStep, "rawStep");
    }

    public static List<WorkflowStep> createSteps(final List<Map<String, Object>> setupSteps) {
        return setupSteps.stream().map(WorkflowStep::createStep).toList();
    }

    public static WorkflowStep createStep(final Map<String, Object> buildStep) {
        return new WorkflowStep(buildStep);
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
