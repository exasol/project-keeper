package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

class PackageJson {

    private final Path path;
    private final String moduleName;
    private final String version;
    private final List<VersionedDependency> dependencies;

    PackageJson(final Path path, final String moduleName, final String version,
            final List<VersionedDependency> dependencies) {
        this.path = path;
        this.moduleName = moduleName;
        this.version = version;
        this.dependencies = dependencies;
    }

    Path getPath() {
        return this.path;
    }

    Path getWorkingDir() {
        return this.path.getParent();
    }

    String getModuleName() {
        return this.moduleName;
    }

    String getVersion() {
        return this.version;
    }

    List<VersionedDependency> getDependencies() {
        return this.dependencies;
    }

    List<VersionedDependency> getDependencies(final Type type) {
        return this.dependencies.stream().filter(d -> d.getType() == type).toList();
    }
}
