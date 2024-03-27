package com.exasol.projectkeeper.validators.files;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

class GitHubWorkflow {

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
            return step -> step.getId().isPresent() && step.getId().get().equals(id);
        }

        public void replaceStep(final String stepId, final Map<String, Object> rawStep) {
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            allSteps.set(index, rawStep);
        }

        private int findStepIndex(final String stepId) {
            final List<Step> steps = getSteps();
            return IntStream.range(0, steps.size()) //
                    .filter(index -> steps.get(index).getId().get().equals(stepId)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalStateException("No step found for id '" + stepId + "' in " + rawJob));
        }
    }

    static class Step {
        private final Map<String, Object> rawStep;

        Step(final Map<String, Object> rawStep) {
            this.rawStep = rawStep;
        }

        Optional<String> getId() {
            return getOptionalString("id");
        }

        Optional<String> getOptionalString(final String key) {
            return Optional.ofNullable((String) rawStep.get(key));
        }

        String getString(final String key) {
            return getOptionalString(key)
                    .orElseThrow(() -> new IllegalStateException("Step has no field '" + key + "': " + rawStep));
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
    }
}
