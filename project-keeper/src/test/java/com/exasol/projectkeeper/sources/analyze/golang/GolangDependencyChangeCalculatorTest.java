package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.*;

@ExtendWith(MockitoExtension.class)
class GolangDependencyChangeCalculatorTest {

    private static final Path MODULE_GO_MOD = Paths.get("./module/go.mod");
    private static final Path PROJECT_DIR = Paths.get("project-dir");
    @Mock
    private GolangServices golangServicesMock;

    @Test
    void noChanges() {
        simulateChanges();
        assertReport(calculate(), emptyMap());
    }

    @Test
    void singleCompileDependency() {
        verifySingleChange(change("dep1", "v1"), Type.COMPILE);
    }

    @Test
    void singleTestDependency() {
        verifySingleChange(change("dep1", "v1"), Type.TEST);
    }

    @Test
    void golangCategorizedAsCompile() {
        final DependencyChange change1 = change("golang", "v1");
        simulateChanges(change1);
        assertReport(calculate(), Type.COMPILE, change1);
    }

    @Test
    void mixedDependencies() {
        final DependencyChange testChange = change("dep1", "v1");
        final DependencyChange compileChange = change("dep2", "v2");
        simulateChanges(testChange, compileChange);
        assertReport(calculate(dep(testChange, Type.TEST), //
                dep(compileChange, Type.COMPILE)), //
                Map.of(Type.COMPILE, List.of(compileChange), //
                        Type.TEST, List.of(testChange)));
    }

    @Test
    void missingDependencyTypeUnkownSuffix() {
        final DependencyChange change1 = change("dep1", "v1");
        simulateChanges(change1);
        assertReport(calculate(), Type.UNKNOWN, change1);
    }

    @Test
    void missingDependencyTypeValidSuffix() {
        final DependencyChange change1 = change("dep1/v1", "v1");
        simulateChanges(change1);
        assertReport(calculate(), Map.of(Type.UNKNOWN, List.of(change1)));
    }

    @Test
    void dependencySuffixChanged() {
        final DependencyChange change1 = change("example.com/mod/v3", "v3.1.0");
        simulateChanges(change1);
        assertReport(calculate(dep(change("example.com/mod/v2", "v2.0.0"), Type.TEST)),
                Map.of(Type.TEST, List.of(change("example.com/mod/v3", "v3.1.0"))));
    }

    private void verifySingleChange(final DependencyChange change, final Type type) {
        simulateChanges(change);
        assertReport(calculate(dep(change, type)), type, change);
    }

    private ProjectDependency dep(final DependencyChange change, final Type type) {
        return dep(change.getArtifactId(), type);
    }

    private ProjectDependency dep(final String name, final Type type) {
        return ProjectDependency.builder() //
                .type(type) //
                .name(name) //
                .licenses(List.of()) //
                .build();
    }

    private void assertReport(final DependencyChangeReport report, final Type type, final DependencyChange expected) {
        assertReport(report, Map.of(type, List.of(expected)));
    }

    private void assertReport(final DependencyChangeReport report, final Map<Type, List<DependencyChange>> expected) {
        for (final Type type : Type.values()) {
            final List<DependencyChange> list = expected.get(type);
            if (list == null) {
                assertThat(report.getChanges(type), empty());
            } else {
                assertThat(report.getChanges(type), containsInAnyOrder(list.toArray(new DependencyChange[0])));
            }
        }
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
