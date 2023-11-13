package com.exasol.projectkeeper.shared.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProjectDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectDependency.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }

    @Test
    void testToString() {
        assertThat(
                ProjectDependency.builder().licenses(List.of(new License("lic", "licUrl"))).name("name")
                        .type(Type.COMPILE).websiteUrl("url").build(),
                hasToString("COMPILE dependency 'name', url, 1 licenses: lic licUrl"));
    }
}
