package com.exasol.projectkeeper.shared.config.workflow;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class WorkflowStepTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(WorkflowStep.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(WorkflowStep.class).verify();
    }

    @Test
    void getId() {
        final WorkflowStep step = WorkflowStep.createStep(Map.of("id", "step-id"));
        assertThat(step.getId(), equalTo("step-id"));
    }

    @Test
    void getIdMissing() {
        final WorkflowStep step = WorkflowStep.createStep(Map.of());
        assertThat(step.getId(), nullValue());
    }

    @Test
    void getRawObject() {
        final Map<String, Object> raw = Map.of("id", "step-id");
        final WorkflowStep step = WorkflowStep.createStep(raw);
        assertThat(step.getRawStep(), sameInstance(raw));
    }
}
