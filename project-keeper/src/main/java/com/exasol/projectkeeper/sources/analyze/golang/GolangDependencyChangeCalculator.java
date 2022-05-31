package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

class GolangDependencyChangeCalculator {

    private final GolangServices golangServices;
    private final Path projectDir;
    private final Source source;
    private final ProjectDependencies dependencies;
    private List<DependencyChange> changes;

    public GolangDependencyChangeCalculator(final GolangServices golangServices, final Path projectDir,
            final Source source, final ProjectDependencies dependencies) {
        this.golangServices = golangServices;
        this.projectDir = projectDir;
        this.source = source;
        this.dependencies = dependencies;
    }

    static DependencyChangeReport calculateDepencencyChanges(final GolangServices golangServices, final Path projectDir,
            final Source source, final ProjectDependencies dependencies) {
        return new GolangDependencyChangeCalculator(golangServices, projectDir, source, dependencies).calculate();
    }

    private DependencyChangeReport calculate() {
        this.changes = this.golangServices.getDependencyChanges(this.projectDir, this.source.getPath());
        final DependencyChangeReport dependencyChanges = new DependencyChangeReport();
        dependencyChanges.setCompileDependencyChanges(getDependencyChanges(Type.COMPILE));
        dependencyChanges.setPluginDependencyChanges(getDependencyChanges(Type.PLUGIN));
        dependencyChanges.setRuntimeDependencyChanges(getDependencyChanges(Type.RUNTIME));
        dependencyChanges.setTestDependencyChanges(getDependencyChanges(Type.TEST));
        return dependencyChanges;
    }

    private List<DependencyChange> getDependencyChanges(final Type type) {
        return this.changes.stream().filter(change -> getType(change) == type).collect(toList());
    }

    private Type getType(final DependencyChange change) {
        final String name = change.getArtifactId();
        if (name.equals(GolangServices.GOLANG_DEPENDENCY_NAME)) {
            return Type.COMPILE;
        }
        return this.dependencies.getDependencies().stream() //
                .filter(dep -> dep.getName().equals(name)) //
                .map(ProjectDependency::getType) //
                .findFirst() //
                .orElseThrow(() -> new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-148").message(
                        "Error finding type of dependency {{artifactId}}, all available dependencies: {{all dependencies}}",
                        name, this.dependencies.getDependencies()).ticketMitigation().toString()));
    }
}