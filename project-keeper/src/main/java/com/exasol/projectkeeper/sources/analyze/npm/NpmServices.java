package com.exasol.projectkeeper.sources.analyze.npm;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

class NpmServices {

    private final CommandExecutor executor;

    NpmServices(final CommandExecutor executor) {
        this.executor = executor;
    }

    ProjectDependencies getDependencies(final PackageJson packageJson) {
        return new ProjectDependencies(new NpmDependencies(this.executor, packageJson).getDependencies());
    }

    DependencyChangeReport getDependencyChangeReport(final PackageJson packageJson) {
        return new NpmDependencyChanges(packageJson).getReport();
    }
}
