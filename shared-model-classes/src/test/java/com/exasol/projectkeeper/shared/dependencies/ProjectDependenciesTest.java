package com.exasol.projectkeeper.shared.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class ProjectDependenciesTest {

    private static final ProjectDependencies PROJECT_DEPENDENCIES = new ProjectDependencies(
            List.of(new ProjectDependency("My Project", "https://example.com",
                    List.of(new License("My License", "https://license.example.com")),
                    ProjectDependency.Type.COMPILE)));

    @Test
    void testSerialize() {
        assertThat(PROJECT_DEPENDENCIES.toJson(), Matchers.equalTo(
                "{\"dependencies\":[{\"licenses\":[{\"name\":\"My License\",\"url\":\"https://license.example.com\"}],\"name\":\"My Project\",\"type\":\"COMPILE\",\"websiteUrl\":\"https://example.com\"}]}"));
    }

    @Test
    void testDeserialize() {
        assertThat(ProjectDependencies.fromJson(PROJECT_DEPENDENCIES.toJson()), Matchers.equalTo(PROJECT_DEPENDENCIES));
    }
}