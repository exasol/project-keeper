package com.exasol.projectkeeper.shared.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class DependencyChangeReportTest {
    private static final DependencyChangeReport REPORT = new DependencyChangeReport(Collections.emptyList(),
            List.of(new NewDependency("com.example", "my-test", "1.2.3"),
                    new RemovedDependency("com.example", "my-other-lib", "1.2.3"),
                    new UpdatedDependency("com.example", "my-updated-dependency", "1.0.0", "1.0.1")),
            Collections.emptyList(), Collections.emptyList());

    @Test
    void testSerialize() {
        assertThat(REPORT.toJson(), equalTo(
                "{\"compileDependencyChanges\":[],\"pluginDependencyChanges\":[],\"runtimeDependencyChanges\":[{\"NewDependency\":{\"artifactId\":\"my-test\",\"groupId\":\"com.example\",\"version\":\"1.2.3\"}},{\"RemovedDependency\":{\"artifactId\":\"my-other-lib\",\"groupId\":\"com.example\",\"version\":\"1.2.3\"}},{\"UpdatedDependency\":{\"artifactId\":\"my-updated-dependency\",\"groupId\":\"com.example\",\"newVersion\":\"1.0.1\",\"version\":\"1.0.0\"}}],\"testDependencyChanges\":[]}"));
    }

    @Test
    void testDeserialize() {
        assertThat(DependencyChangeReport.fromJson(REPORT.toJson()), equalTo(REPORT));
    }
}