package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Path;

import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;

class NpmServices {

    CommandExecutor executor;

    NpmServices(final CommandExecutor executor) {
        this.executor = executor;
    }

//    PackageJson readPackageJson(final Path projectDir) {
//        return PackageJson.read(projectDir);
//    }

    ProjectDependencies getDependencies(final Path projectDir) {
        final PackageJson json = PackageJsonReader.read(projectDir);
        return new ProjectDependencies(new NpmDependencies(this.executor, json).getDependencies());
    }

    DependencyChangeReport getDependencyChangeReport(final PackageJson packageJson) {
        return new NpmDependencyChanges(packageJson).getReport();
    }
}
