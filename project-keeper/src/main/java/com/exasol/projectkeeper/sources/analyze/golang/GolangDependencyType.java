package com.exasol.projectkeeper.sources.analyze.golang;

import java.util.*;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependency;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;

class GolangDependencyType {
    private final List<ProjectDependency> dependencies;
    private final Map<String, Type> types;

    GolangDependencyType(final List<ProjectDependency> dependencies) {
        this.dependencies = dependencies;
        this.types = dependencies.stream()
                .collect(Collectors.toMap(ProjectDependency::getName, ProjectDependency::getType));
    }

    Type getType(final DependencyChange change) {
        final String moduleName = change.getArtifactId();
        if (isGolangRuntime(moduleName)) {
            return Type.COMPILE;
        }
        final Type type = this.types.get(moduleName);
        if (type != null) {
            return type;
        }
        return getTypeByPrefix(moduleName);
    }

    private boolean isGolangRuntime(final String name) {
        return name.equals(GolangServices.GOLANG_DEPENDENCY_NAME);
    }

    private Type getTypeByPrefix(final String moduleName) {
        if (moduleName.matches(".*/v\\d+")) {
            final String moduleNamePrefix = moduleName.substring(0, moduleName.lastIndexOf("/"));
            GolangDependencyChangeCalculator.LOGGER
                    .finest(() -> "Found prefix '" + moduleNamePrefix + "' for module '" + moduleName + "'.");
            final Optional<Type> type = this.dependencies.stream() //
                    .filter(dep -> dep.getName().startsWith(moduleNamePrefix)) //
                    .map(ProjectDependency::getType) //
                    .findFirst();
            if (type.isPresent()) {
                return type.get();
            } else {
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-159")
                        .message("Error finding type of module prefix {{module name prefix}}," //
                                + " all available dependencies: {{all dependencies}}.", //
                                moduleNamePrefix, this.dependencies)
                        .ticketMitigation().toString());
            }
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-158")
                    .message("Unknown suffix for module {{module name}}.", moduleName).ticketMitigation().toString());
        }
    }

}