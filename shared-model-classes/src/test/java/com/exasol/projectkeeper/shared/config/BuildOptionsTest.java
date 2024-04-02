package com.exasol.projectkeeper.shared.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.workflow.WorkflowOptions;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class BuildOptionsTest {

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(BuildOptions.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(BuildOptions.class).verify();
    }

    @Test
    void builderSetNullWorkflows() {
        final BuildOptions options = BuildOptions.builder().workflows(null).build();
        assertThat(options.getWorkflows(), empty());
    }

    @Test
    void getWorkflowByNameMissing() {
        final BuildOptions options = BuildOptions.builder().build();
        assertThat(options.getWorkflow("missing").isPresent(), is(false));
    }

    @Test
    void getWorkflowByNamePresent() {
        final WorkflowOptions workflow = WorkflowOptions.builder().workflowName("name").build();
        final BuildOptions options = BuildOptions.builder().workflows(List.of(workflow)).build();
        final Optional<WorkflowOptions> foundWorkflow = options.getWorkflow("name");
        assertAll(() -> assertThat(foundWorkflow.isPresent(), is(true)),
                () -> assertThat(foundWorkflow.get(), sameInstance(workflow)));
    }
}
