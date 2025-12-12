package com.exasol.projectkeeper.shared.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProjectDependencyTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ProjectDependency.class).verify();
    }

    @Test
    void testToString() {
        assertThat(
                ProjectDependency.builder().licenses(List.of(new License("lic", "licUrl"))).name("name")
                        .coordinates("coord")
                        .type(Type.COMPILE).websiteUrl("url").build(),
                hasToString("COMPILE dependency 'name', (coord), url, 1 licenses: lic licUrl"));
    }

    @Test
    void testCopy() {
        final ProjectDependency dep = ProjectDependency.builder().licenses(List.of(new License("lic", "licUrl")))
                .name("name").coordinates("coord").type(Type.COMPILE).websiteUrl("url").build();
        assertThat(dep.copy().build(), equalTo(dep));
    }
}
