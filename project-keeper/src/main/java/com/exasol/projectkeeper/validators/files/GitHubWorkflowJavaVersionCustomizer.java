package com.exasol.projectkeeper.validators.files;

import static java.util.stream.Collectors.joining;

import java.util.List;

import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class GitHubWorkflowJavaVersionCustomizer implements GitHubWorkflowCustomizer.WorkflowCustomizer {

    private final List<String> javaVersions;

    GitHubWorkflowJavaVersionCustomizer(final List<String> javaVersions) {
        this.javaVersions = javaVersions;
    }

    @Override
    public void applyCustomization(final GitHubWorkflow workflow) {
        workflow.getJobs().forEach(this::updateJavaVersion);
    }

    private void updateJavaVersion(final Job job) {
        job.getSteps().stream().filter(this::isSetupJavaStep).forEach(this::updateJavaVersion);
    }

    private void updateJavaVersion(final Step step) {
        step.getWith().put("java-version", formatJavaVersions());
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
