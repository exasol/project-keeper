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

class WorkflowOptionsTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(WorkflowOptions.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(WorkflowOptions.class).verify();
    }

    @Test
    void builderDefaultCustomizationEmpty() {
        final WorkflowOptions options = WorkflowOptions.builder().workflowName("name").build();
        assertThat(options.getCustomizations(), empty());
    }

    @Test
    void builderAddCustomization() {
        final WorkflowOptions options = WorkflowOptions.builder().workflowName("name")
                .addCustomization(StepCustomization.builder().type(Type.REPLACE).stepId("stepId")
                        .step(WorkflowStep.createStep(emptyMap())).build())
                .build();
        assertThat(options.getCustomizations(), hasSize(1));
    }

    @Test
    void builderSetCustomizations() {
        final WorkflowOptions options = WorkflowOptions.builder().workflowName("name")
                .customizations(List.of(StepCustomization.builder().type(Type.REPLACE).stepId("stepId")
                        .step(WorkflowStep.createStep(emptyMap())).build()))
                .build();
        assertThat(options.getCustomizations(), hasSize(1));
    }
}
