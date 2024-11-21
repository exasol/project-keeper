package com.exasol.projectkeeper.validators.files;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.workflow.WorkflowStep;

/**
 * This class represents a GitHub workflow and provides methods to access its content via getter methods.
 */
class GitHubWorkflow {
    private static final Logger LOG = Logger.getLogger(GitHubWorkflow.class.getName());
    private final Map<String, Object> rawWorkflow;

    private GitHubWorkflow(final Map<String, Object> rawWorkflow) {
        this.rawWorkflow = rawWorkflow;
    }

    static GitHubWorkflow create(final Object rawWorkflow) {
        return new GitHubWorkflow(asMap(rawWorkflow));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(final Object rawYaml) {
        return (Map<String, Object>) rawYaml;
    }

    Job getJob(final String jobId) {
        final Object rawJob = getJobMap().get(jobId);
        if (rawJob == null) {
            return null;
        }
        return new Job(jobId, asMap(rawJob));
    }

    private Map<String, Object> getJobMap() {
        return asMap(rawWorkflow.get("jobs"));
    }

    List<Job> getJobs() {
        return getJobMap().entrySet().stream().map(entry -> new Job(entry.getKey(), asMap(entry.getValue()))).toList();
    }

    Map<String, Object> getOnTrigger() {
        return asMap(rawWorkflow.get("on"));
    }

    Object getRawObject() {
        return this.rawWorkflow;
    }

    /**
     * This class represents a job in a GitHub workflow and provides methods to access its content via getter methods as
     * well as insert and replace steps.
     */
    static class Job {
        private final String id;
        private final Map<String, Object> rawJob;

        private Job(final String id, final Map<String, Object> rawJob) {
            this.id = id;
            this.rawJob = rawJob;
        }

        String getId() {
            return id;
        }

        Step getStep(final String id) {
            return getSteps().stream() //
                    .filter(hasId(id)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-206")
                            .message("Job has no step with ID {{step id}}: {{raw job}}", id, rawJob).toString()));
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

        public String getEnvironment() {
            return (String) rawJob.get("environment");
        }

        public void setEnvironment(final String environmentName) {
            rawJob.put("environment", environmentName);
        }

        private Predicate<? super Step> hasId(final String id) {
            return step -> step.getId().equals(id);
        }

        // [impl->dsn~customize-build-process.replace-step~0]
        public void replaceStep(final String stepId, final WorkflowStep step) {
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Replacing step '" + stepId + "' at index " + index + " with step '" + step.getId() + "'");
            allSteps.set(index, step.getRawStep());
        }

        // [impl->dsn~customize-build-process.insert-step-after~0]
        public void insertStepAfter(final String stepId, final WorkflowStep step) {
            final List<Map<String, Object>> allSteps = getRawSteps();
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Inserting step '" + step.getId() + "' at index " + (index + 1) + " after step '" + stepId
                    + "'");
            allSteps.add(index + 1, step.getRawStep());
        }

        private int findStepIndex(final String stepId) {
            final List<Step> steps = getSteps();
            return IntStream.range(0, steps.size()) //
                    .filter(index -> steps.get(index).getId().equals(stepId)) //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-205")
                            .message("No step found for id {{step id}} in {{available step ids}}", stepId,
                                    steps.stream().map(Step::getId).toList())
                            .toString()));
        }
    }

    /**
     * This class represents a step in a GitHub workflow and provides methods to access its content via getter methods.
     */
    static class Step {
        private final Map<String, Object> rawStep;

        private Step(final Map<String, Object> rawStep) {
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

        boolean isUsesAction() {
            return getOptionalString("uses").isPresent();
        }

        public Map<String, Object> getWith() {
            return asMap(rawStep.computeIfAbsent("with", key -> new HashMap<>()));
        }

        private String getString(final String key) {
            return getOptionalString(key)
                    .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-204")
                            .message("Step has no field {{field name}}: {{raw step}}", key, rawStep).toString()));
        }

        private Optional<String> getOptionalString(final String key) {
            return Optional.ofNullable((String) rawStep.get(key));
        }
    }

    public Job getJob(final Optional<String> jobId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getJob'");
    }
}
