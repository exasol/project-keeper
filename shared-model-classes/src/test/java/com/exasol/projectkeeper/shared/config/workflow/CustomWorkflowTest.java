package com.exasol.projectkeeper.shared.config.workflow;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.config.workflow.StepCustomization.Type;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class CustomWorkflowTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(CustomWorkflow.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(CustomWorkflow.class).verify();
    }

    @Test
    void builderDefaultCustomizationEmpty() {
        final CustomWorkflow options = CustomWorkflow.builder().workflowName("name").build();
        assertThat(options.getSteps(), empty());
    }

    @Test
    void builderAddCustomization() {
        final CustomWorkflow options = CustomWorkflow.builder().workflowName("name").addStep(StepCustomization.builder()
                .jobId("myJob").type(Type.REPLACE).stepId("stepId").step(WorkflowStep.createStep(emptyMap())).build())
                .build();
        assertThat(options.getSteps(), hasSize(1));
    }

    @Test
    void builderSetCustomizations() {
        final CustomWorkflow options = CustomWorkflow
                .builder().workflowName("name").steps(List.of(StepCustomization.builder().jobId("myJob")
                        .type(Type.REPLACE).stepId("stepId").step(WorkflowStep.createStep(emptyMap())).build()))
                .build();
        assertThat(options.getSteps(), hasSize(1));
    }
}
