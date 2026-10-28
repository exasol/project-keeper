package com.exasol.projectkeeper.validators.files;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import org.snakeyaml.engine.v2.nodes.Node;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.workflow.JobPermissions;
import com.exasol.projectkeeper.shared.config.workflow.WorkflowStep;

/**
 * This class represents a GitHub workflow and provides methods to access its content via getter methods.
 */
class GitHubWorkflow {
    private static final Logger LOG = Logger.getLogger(GitHubWorkflow.class.getName());
    private final YamlMapping rawWorkflow;

    private GitHubWorkflow(final YamlMapping rawWorkflow) {
        this.rawWorkflow = rawWorkflow;
    }

    static GitHubWorkflow create(final Object rawWorkflow) {
        return new GitHubWorkflow(YamlMapping.from(rawWorkflow));
    }

    Job getJob(final String jobId) {
        final YamlMapping jobs = getJobMap();
        if (!jobs.contains(jobId)) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-207")
                    .message("GitHub Workflow does not have a job with ID {{job id}}.", jobId)
                    .mitigation("Choose one of {{available job ids}}.", jobs.keys()).toString());
        }
        return new Job(jobId, jobs.mapping(jobId));
    }

    boolean hasJob(final String jobName) {
        return getJobMap().contains(jobName);
    }

    void removeJob(final String jobName) {
        final YamlMapping jobs = getJobMap();
        if (jobs.remove(jobName) == null) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-PK-CORE-211")
                    .message("GitHub Workflow does not have a job with ID {{job name}}.", jobName)
                    .mitigation("Choose one of {{available job names}}.", jobs.keys()).toString());
        }
    }

    private YamlMapping getJobMap() {
        return rawWorkflow.mapping("jobs");
    }

    List<Job> getJobs() {
        return getJobMap().mappings().entrySet().stream().map(entry -> new Job(entry.getKey(), entry.getValue()))
                .toList();
    }

    Map<String, Object> getOnTrigger() {
        return rawWorkflow.objectMap("on");
    }

    Object getRawObject() {
        return rawWorkflow.toObject();
    }

    Node getRawNode() {
        return rawWorkflow.node();
    }

    /**
     * This class represents a job in a GitHub workflow and provides methods to access its content via getter methods as
     * well as insert and replace steps.
     */
    static class Job {
        private static final String STEPS_KEY = "steps";
        private final String id;
        private final YamlMapping rawJob;

        private Job(final String id, final YamlMapping rawJob) {
            this.id = id;
            this.rawJob = rawJob;
        }

        String getId() {
            return id;
        }

        Step getStep(final String id) {
            return getSteps().stream()
                    .filter(hasId(id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-206")
                            .message("Job has no step with ID {{step id}}: {{raw job}}", id, rawJob.toObject())
                            .toString()));
        }

        List<Step> getSteps() {
            return rawJob.mappingList(STEPS_KEY).stream().map(Step::new).toList();
        }

        public List<String> getNeeds() {
            return rawJob.stringList("needs");
        }

        void removeNeed(final String jobName) {
            rawJob.removeFromSequence("needs", jobName);
        }

        String getRunnerOS() {
            return rawJob.string("runs-on");
        }

        Map<String, Object> getConcurrency() {
            return rawJob.objectMap("concurrency");
        }

        Map<String, Object> getStrategy() {
            return rawJob.objectMap("strategy");
        }

        Map<String, Object> getEnv() {
            return rawJob.objectMap("env");
        }

        public String getEnvironment() {
            return rawJob.optionalString("environment").orElse(null);
        }

        public void setEnvironment(final String environmentName) {
            rawJob.put("environment", environmentName);
        }

        @SuppressWarnings("unchecked")
        public Map<String, String> getPermissions() {
            return (Map<String, String>) (Map<?, ?>) rawJob.objectMap("permissions");
        }

        public void setPermissions(final JobPermissions permissions) {
            final Map<String, String> rawPermissions = new LinkedHashMap<>();
            permissions.getPermissions()
                    .forEach((name, permission) -> rawPermissions.put(name, permission.getName()));
            rawJob.put("permissions", rawPermissions);
        }

        private Predicate<? super Step> hasId(final String id) {
            return step -> step.getId().equals(id);
        }

        // [impl->dsn~customize-build-process.replace-step~0]
        public void replaceStep(final String stepId, final WorkflowStep step) {
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Replacing step '" + stepId + "' at index " + index + " with step '" + step.getId() + "'");
            rawJob.replaceInSequence(STEPS_KEY, index, step.getRawStep());
        }

        // [impl->dsn~customize-build-process.insert-step-after~0]
        public void insertStepAfter(final String stepId, final WorkflowStep step) {
            final int index = findStepIndex(stepId);
            LOG.fine(() -> "Inserting step '" + step.getId() + "' at index " + (index + 1) + " after step '" + stepId
                    + "'");
            rawJob.insertIntoSequence(STEPS_KEY, index + 1, step.getRawStep());
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
        private final YamlMapping rawStep;

        private Step(final YamlMapping rawStep) {
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
            return rawStep.optionalString("uses").isPresent();
        }

        public Map<String, Object> getWith() {
            if (rawStep.isNull("with")) {
                return Collections.emptyMap();
            }
            return rawStep.objectMap("with");
        }

        void setWithValue(final String key, final Object value) {
            rawStep.putInMapping("with", key, value);
        }

        private String getString(final String key) {
            return rawStep.optionalString(key)
                    .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-204")
                            .message("Step has no field {{field name}}: {{raw step}}", key, rawStep.toObject())
                            .toString()));
        }
    }
}
