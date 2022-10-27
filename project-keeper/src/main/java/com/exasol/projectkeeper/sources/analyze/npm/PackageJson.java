package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;
import com.exasol.projectkeeper.sources.analyze.generic.PreviousRelease;

class PackageJson {

    private final Path path;
    private final String moduleName;
    private final String version;
    private final List<VersionedDependency> dependencies;

    PackageJson(final Path projectDir, final String moduleName, final String version,
            final List<VersionedDependency> dependencies) {
        this.path = projectDir;
        this.moduleName = moduleName;
        this.version = version;
        this.dependencies = dependencies;
    }

    PreviousRelease previousRelease() {
        return new PreviousRelease(this.path, this.version);
    }

    Path getPath() {
        return this.path;
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
        return this.dependencies.stream().filter(d -> d.getType() == type).collect(Collectors.toList());
    }
}
