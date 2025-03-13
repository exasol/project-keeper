package com.exasol.projectkeeper.validators.changesfile.dependencies;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;
import java.util.stream.Stream;

import org.apache.maven.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.*;

class DependencyChangeReportReaderTest {

    private static final DependencyChangeReportReader READER = new DependencyChangeReportReader();

    private static Stream<Arguments> compileScopes() {
        return Stream.of(//
                Arguments.of((Object) null), //
                Arguments.of(""), //
                Arguments.of("compile")//
        );
    }

    @ParameterizedTest
    @MethodSource("compileScopes")
    void testCompareCompileDependencies(final String scope) {
        final Model newModel = getNewModel();
        final Dependency dependency = buildDependency(scope);
        newModel.addDependency(dependency);
        final Model oldModel = getNewModel();
        final DependencyChangeReport report = READER.read(oldModel, newModel);
        assertThat(report.getChanges(Type.COMPILE), contains(new NewDependency("com.example", "my-lib", "0.1.0")));
    }

    @Test
    void testCompareCompileDependenciesHaveAlphabeticalOrder() {
        final Model newModel = getNewModel();
        newModel.addDependency(buildDependency("b", ""));
        newModel.addDependency(buildDependency("c", ""));
        newModel.addDependency(buildDependency("a", ""));
        final Model oldModel = getNewModel();
        final DependencyChangeReport report = READER.read(oldModel, newModel);
        final List<String> artifactIds = report.getChanges(Type.COMPILE).stream().map(DependencyChange::getArtifactId)
                .toList();
        assertThat(artifactIds, contains("a", "b", "c"));
    }

    @Test
    void testCompareTestDependencies() {
        final Model newModel = getNewModel();
        final Dependency dependency = buildDependency("test");
        newModel.addDependency(dependency);
        final Model oldModel = getNewModel();
        final DependencyChangeReport report = READER.read(oldModel, newModel);
        assertThat(report.getChanges(Type.TEST), contains(new NewDependency("com.example", "my-lib", "0.1.0")));
    }

    @Test
    void testComparePluginDependencies() {
        final Model newModel = getNewModel();
        final Plugin plugin = new Plugin();
        plugin.setArtifactId("my-plugin");
        plugin.setGroupId("com.example");
        plugin.setVersion("0.1.0");
        newModel.getBuild().addPlugin(plugin);
        final Model oldModel = getNewModel();
        oldModel.setBuild(new Build());
        final DependencyChangeReport report = new DependencyChangeReportReader().read(oldModel, newModel);
        assertThat(report.getChanges(Type.PLUGIN), contains(new NewDependency("com.example", "my-plugin", "0.1.0")));
    }

    private Dependency buildDependency(final String scope) {
        return buildDependency("my-lib", scope);
    }

    private Dependency buildDependency(final String name, final String scope) {
        final Dependency dependency = new Dependency();
        dependency.setArtifactId(name);
        dependency.setGroupId("com.example");
        dependency.setVersion("0.1.0");
        dependency.setScope(scope);
        return dependency;
    }

    private Model getNewModel() {
        final Model model = new Model();
        model.setBuild(new Build());
        return model;
    }
}
