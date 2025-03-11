package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GitHubWorkflowEnvironmentCustomizerTest {

    @Test
    void updateEnvironment() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                """, new GitHubWorkflowEnvironmentCustomizer("build", "my-env"));
        assertThat(workflow.getJob("build").getEnvironment(), equalTo("my-env"));
    }

    @Test
    void environmentIsNull() {
        final GitHubWorkflow workflow = customize("""
                jobs:
                  build:
                    steps:
                """, new GitHubWorkflowEnvironmentCustomizer("build", null));
        assertThat(workflow.getJob("build").getEnvironment(), nullValue());
    }

    @Test
    void unknownJobId() {
        final GitHubWorkflowEnvironmentCustomizer customizer = new GitHubWorkflowEnvironmentCustomizer("unknown-job",
                "my-env");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> customize("""
                jobs:
                  build:
                    steps:
                """, customizer));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-207: GitHub Workflow does not have a job with ID 'unknown-job'. Choose one of ['build']."));
    }

    GitHubWorkflow customize(final String yaml, final GitHubWorkflowCustomizer.WorkflowCustomizer customizer) {
        final GitHubWorkflow workflow = GitHubWorkflowIO.create().loadWorkflow(yaml);
        customizer.applyCustomization(workflow);
        return workflow;
    }
}
