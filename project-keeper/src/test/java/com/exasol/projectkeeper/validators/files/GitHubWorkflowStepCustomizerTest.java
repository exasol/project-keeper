package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.BuildOptions;

class GitHubWorkflowStepCustomizerTest {

    @Test
    void noChangesWithEmptyConfiguration() {
        assertThat(validate(BuildOptions.builder(), """
                name: CI
                'on':
                  push:
                    branches: [main]
                """), equalTo("""
                name: CI
                'on':
                  push:
                    branches: [main]
                """));
    }

    private String validate(final BuildOptions.Builder optionsBuilder, final String workflowTemplate) {
        return new GitHubWorkflowStepCustomizer(optionsBuilder.build()).customizeContent(workflowTemplate);
    }
}
