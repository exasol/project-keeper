package com.exasol.projectkeeper.validators.files;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.exasol.projectkeeper.shared.config.WorkflowStep;

class GitHubWorkflow {
    private static final Logger LOG = Logger.getLogger(GitHubWorkflow.class.getName());
    private final Map<String, Object> rawWorkflow;

    GitHubWorkflow(final Map<String, Object> rawWorkflow) {
        this.rawWorkflow = rawWorkflow;
    }

    static GitHubWorkflow create(final Object rawWorkflow) {
        return new GitHubWorkflow(asMap(rawWorkflow));
    }

    Job getJob(final String jobId) {
        final Map<String, Object> jobs = asMap(rawWorkflow.get("jobs"));
        return new Job(asMap(jobs.get(jobId)));
    }

    Map<String, Object> getOnTrigger() {
        return asMap(rawWorkflow.get("on"));
    }

    Object getRawObject() {
        return this.rawWorkflow;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(final Object rawYaml) {
        return (Map<String, Object>) rawYaml;
    }

    static class Job {
        private final Map<String, Object> rawJob;

        Job(final Map<String, Object> rawJob) {
            this.rawJob = rawJob;
        }

        Step getStep(final String id) {
            return getSteps().stream() //
                    .filter(hasId(id)) //
                    .findFirst() //
                    .orElseThrow(
                            () -> new IllegalStateException("Job has no step with ID '" + id + "': " + getRawSteps()));
        }

        List<Step> getSteps() {
            return getRawSteps().stream().map(Step::new).toList();
        }

        @SuppressWarnings("unchecked")
        private List<Map<String, Object>> getRawSteps() {
            final Object steps = rawJob.get("steps");
            if (steps == null) {
                return emptyList();
            }
            return (List<Map<String, Object>>) steps;
        }

        String getRunnerOS() {
            return (String) rawJob.get("runs-on");
        }

        Map<String, Object> getConcurrency() {
            return asMap(rawJob.get("concurrency"));
        }

        Map<String, Object> getStrategy() {
            return asMap(rawJob.get("strategy"));
        }

        Map<String, Object> getEnv() {
            return asMap(rawJob.get("env"));
        }

        private Predicate<? super Step> hasId(final String id) {
            return step -> step.getId().equals(id);
        }

        public void replaceStep(final String stepId, final Map<String, Object> rawStep) {
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            allSteps.set(index, rawStep);
        }

        public void insertStepsBefore(final String stepId, final List<WorkflowStep> steps) {
            if (steps.isEmpty()) {
                return;
            }
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Inserting " + steps.size() + " setup steps at index " + index + " before step '" + stepId
                    + "'");
            allSteps.addAll(index, getRawSteps(steps));
        }

        public void insertStepsAfter(final String stepId, final List<WorkflowStep> steps) {
            if (steps.isEmpty()) {
                return;
            }
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Inserting " + steps.size() + " cleanup steps at index " + (index + 1) + " after step '"
                    + stepId + "'");
            allSteps.addAll(index + 1, getRawSteps(steps));
        }

        private List<Map<String, Object>> getRawSteps(final List<WorkflowStep> setupSteps) {
            return setupSteps.stream().map(WorkflowStep::getRawStep).toList();
        }

        private int findStepIndex(final String stepId) {
            final List<Step> steps = getSteps();
            return IntStream.range(0, steps.size()) //
                    .filter(index -> steps.get(index).getId().equals(stepId)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalStateException("No step found for id '" + stepId + "' in " + rawJob));
        }
    }

    static class Step {
        private final Map<String, Object> rawStep;

        Step(final Map<String, Object> rawStep) {
            this.rawStep = rawStep;
        }

        String getId() {
            return getString("id");
        }

        String getName() {
            return getString("name");
        }

        String getRunCommand() {
            return getString("run");
        }

        String getIfCondition() {
            return getString("if");
        }

        String getUsesAction() {
            return getString("uses");
        }

        public Map<String, Object> getWith() {
            return asMap(rawStep.get("with"));
        }

        String getString(final String key) {
            return getOptionalString(key)
                    .orElseThrow(() -> new IllegalStateException("Step has no field '" + key + "': " + rawStep));
        }

        Optional<String> getOptionalString(final String key) {
            return Optional.ofNullable((String) rawStep.get(key));
        }
    }
}
