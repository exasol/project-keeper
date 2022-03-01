package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.shared.dependencychanges.*;

class MavenDependencyComparatorTest {

    @Test
    void testAddedDependency() {
        final List<DependencyChange> report = new MavenDependencyComparator().compare(Collections.emptyList(),
                List.of(buildMyLibDependency("1.0.1")));
        assertThat(report, contains(new NewDependency("com.example", "my-lib", "1.0.1")));
    }

    @Test
    void testUpdatedDependency() {
        final List<DependencyChange> report = new MavenDependencyComparator()
                .compare(List.of(buildMyLibDependency("1.0.1")), List.of(buildMyLibDependency("1.0.2")));
        assertThat(report, contains(new UpdatedDependency("com.example", "my-lib", "1.0.1", "1.0.2")));
    }

    @Test
    void testRemovedDependency() {
        final List<DependencyChange> report = new MavenDependencyComparator()
                .compare(List.of(buildMyLibDependency("1.0.1")), Collections.emptyList());
        assertThat(report, contains(new RemovedDependency("com.example", "my-lib", "1.0.1")));
    }

    @Test
    void testUnchangedDependency() {
        final List<Dependency> dependencyList = List.of(buildMyLibDependency("1.0.1"));
        final List<DependencyChange> report = new MavenDependencyComparator().compare(dependencyList, dependencyList);
        assertThat(report, empty());
    }

    private Dependency buildMyLibDependency(final String version) {
        final Dependency dependency = new Dependency();
        dependency.setArtifactId("my-lib");
        dependency.setGroupId("com.example");
        dependency.setVersion(version);
        return dependency;
    }
}