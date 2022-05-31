package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;

@ExtendWith(MockitoExtension.class)
class GolangDependencyChangeCalculatorTest {

    private static final Path MODULE_GO_MOD = Paths.get("./module/go.mod");
    private static final Path PROJECT_DIR = Paths.get("project-dir");
    @Mock
    private GolangServices golangServicesMock;

    @Test
    void testNoChanges() {
        simulateChanges();
        assertReport(calculate(), emptyList(), emptyList());
    }

    @Test
    void singleCompileDependency() {
        final DependencyChange change1 = change("dep1", "v1");
        simulateChanges(change1);
        assertReport(calculate(dep(change1, Type.COMPILE)), List.of(change1), emptyList());
    }

    @Test
    void singleTestDependency() {
        final DependencyChange change1 = change("dep1", "v1");
        simulateChanges(change1);
        assertReport(calculate(dep(change1, Type.TEST)), emptyList(), List.of(change1));
    }

    @Test
    void golangCategorizedAsCompile() {
        final DependencyChange change1 = change("golang", "v1");
        simulateChanges(change1);
        assertReport(calculate(), List.of(change1), List.of());
    }

    @Test
    void mixedDependencies() {
        final DependencyChange testChange = change("dep1", "v1");
        final DependencyChange compileChange = change("dep2", "v2");
        simulateChanges(testChange, compileChange);
        assertReport(calculate(dep(testChange, Type.TEST), dep(compileChange, Type.COMPILE)), List.of(compileChange),
                List.of(testChange));
    }

    @Test
    void missingDependencyType() {
        final DependencyChange change1 = change("dep1", "v1");
        simulateChanges(change1);
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> calculate());
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-148: Error finding type of dependency 'dep1', all available dependencies: []."));
    }

    @Test
    void wrongDependency() {
        final DependencyChange change1 = change("dep1", "v1");
        simulateChanges(change1);
        final ProjectDependency dep = dep("unknown", Type.COMPILE);
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> calculate(dep));
        assertThat(exception.getMessage(), startsWith(
                "E-PK-CORE-148: Error finding type of dependency 'dep1', all available dependencies: [ProjectDependency(name=unknown, websiteUrl=null, licenses=[], type=COMPILE)]."));
    }

    private ProjectDependency dep(final DependencyChange change, final Type type) {
        return dep(change.getArtifactId(), type);
    }

    private ProjectDependency dep(final String name, final Type type) {
        return new ProjectDependency(name, null, List.of(), type);
    }

    private void assertReport(final DependencyChangeReport report,
            final List<DependencyChange> expectedCompileDependencies,
            final List<DependencyChange> expectedTestDependencies) {
        assertAll(() -> assertThat("plugin dependencies", report.getPluginDependencyChanges(), hasSize(0)),
                () -> assertThat("runtime dependencies", report.getRuntimeDependencyChanges(), hasSize(0)),
                () -> assertThat("compile dependencies", report.getCompileDependencyChanges(),
                        containsInAnyOrder(expectedCompileDependencies.toArray(new DependencyChange[0]))),
                () -> assertThat("test dependencies", report.getTestDependencyChanges(),
                        containsInAnyOrder(expectedTestDependencies.toArray(new DependencyChange[0]))));
    }

    private void simulateChanges(final DependencyChange... dependencyChanges) {
        when(this.golangServicesMock.getDependencyChanges(PROJECT_DIR, MODULE_GO_MOD))
                .thenReturn(asList(dependencyChanges));
    }

    private DependencyChange change(final String name, final String version) {
        return new NewDependency(null, name, version);
    }

    private DependencyChangeReport calculate(final ProjectDependency... dependencies) {
        final Source source = Source.builder().path(MODULE_GO_MOD).build();
        return GolangDependencyChangeCalculator.calculateDepencencyChanges(this.golangServicesMock, PROJECT_DIR, source,
                new ProjectDependencies(asList(dependencies)));
    }
}
