package com.exasol.projectkeeper.validators.files;

import static java.util.stream.Collectors.joining;

import java.util.List;

import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class GitHubWorkflowJavaVersionCustomizer implements WorkflowCustomizer {

    private final List<String> javaVersions;
    private final String nextJavaVersion;

    GitHubWorkflowJavaVersionCustomizer(final List<String> javaVersions, final String nextJavaVersion) {
        this.javaVersions = javaVersions;
        this.nextJavaVersion = nextJavaVersion;
    }

    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        for (final Job job : workflow.getJobs()) {
            if ("next-java-compatibility".equals(job.getId())) {
                updateJavaVersion(job, nextJavaVersion);
            } else {
                updateJavaVersion(job, formatJavaVersions());
            }
        }
    }

    private void updateJavaVersion(final Job job, final String version) {
        job.getSteps().stream().filter(this::isSetupJavaStep).forEach(step -> updateJavaVersion(step, version));
    }

    private void updateJavaVersion(final Step step, final String version) {
        step.getWith().put("java-version", version);
    }

    private String formatJavaVersions() {
        return javaVersions.stream().collect(joining("\n"));
    }

    private boolean isSetupJavaStep(final Step step) {
        if (!step.isUsesAction()) {
            return false;
        }
        return step.getUsesAction().startsWith("actions/setup-java@v");
    }
}
