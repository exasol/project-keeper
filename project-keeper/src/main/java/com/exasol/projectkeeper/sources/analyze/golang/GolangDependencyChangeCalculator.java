package com.exasol.projectkeeper.sources.analyze.golang;

import static java.util.stream.Collectors.toList;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Source;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;

class GolangDependencyChangeCalculator {
    private static final Logger LOGGER = Logger.getLogger(GolangDependencyChangeCalculator.class.getName());
    private final GolangServices golangServices;
    private final Path projectDir;
    private final Source source;
    private final ProjectDependencies dependencies;
    private List<DependencyChange> changes;

    GolangDependencyChangeCalculator(final GolangServices golangServices, final Path projectDir, final Source source,
            final ProjectDependencies dependencies) {
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
        this.changes = this.golangServices.getDependencyChanges(this.projectDir, getRelativeModPath());
        final DependencyChangeReport dependencyChanges = new DependencyChangeReport();
        dependencyChanges.setCompileDependencyChanges(getDependencyChanges(Type.COMPILE));
        dependencyChanges.setPluginDependencyChanges(getDependencyChanges(Type.PLUGIN));
        dependencyChanges.setRuntimeDependencyChanges(getDependencyChanges(Type.RUNTIME));
        dependencyChanges.setTestDependencyChanges(getDependencyChanges(Type.TEST));
        return dependencyChanges;
    }

    private Path getRelativeModPath() {
        if (this.source.getPath().isAbsolute()) {
            return this.projectDir.relativize(this.source.getPath());
        } else {
            return this.source.getPath();
        }
    }

    private List<DependencyChange> getDependencyChanges(final Type type) {
        return this.changes.stream().filter(change -> getType(change) == type).collect(toList());
    }

    private Type getType(final DependencyChange change) {
        final String moduleName = change.getArtifactId();
        if (isGolangRuntime(moduleName)) {
            return Type.COMPILE;
        }
        return this.dependencies.getDependencies().stream() //
                .filter(dep -> dep.getName().equals(moduleName)) //
                .map(ProjectDependency::getType) //
                .findFirst() //
                .orElseGet(() -> getTypeByPrefix(moduleName));
    }

    private Type getTypeByPrefix(final String moduleName) {
        if (moduleName.matches(".*/v\\d+")) {
            final String moduleNamePrefix = moduleName.substring(0, moduleName.lastIndexOf("/"));
            LOGGER.finest(() -> "Found prefix '" + moduleNamePrefix + "' for module '" + moduleName + "'.");
            final Optional<Type> type = this.dependencies.getDependencies().stream() //
                    .filter(dep -> dep.getName().startsWith(moduleNamePrefix)) //
                    .map(ProjectDependency::getType) //
                    .findFirst();
            if (type.isPresent()) {
                return type.get();
            } else {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-159").message(
                        "Error finding type of module prefix {{module name prefix}}, all available dependencies: {{all dependencies}}.",
                        moduleNamePrefix, this.dependencies.getDependencies()).ticketMitigation().toString());
            }
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-158")
                    .message("Unknown suffix for module {{module name}}.", moduleName).ticketMitigation().toString());
        }

    }

    private boolean isGolangRuntime(final String name) {
        return name.equals(GolangServices.GOLANG_DEPENDENCY_NAME);
    }
}