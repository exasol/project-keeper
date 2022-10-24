package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;
import com.exasol.projectkeeper.sources.analyze.generic.PreviousRelease;

class PackageJson {

    private final Path projectDir;
    private final String moduleName;
    private final String version;
//    private final JsonObject content;
    private final List<VersionedDependency> dependencies;

//    PackageJson(final Path projectDir, final String moduleName, final String version, final JsonObject content) {
//        this.projectDir = projectDir;
//        this.moduleName = moduleName;
//        this.version = version;
//        this.content = content;
//    }

    PackageJson(final Path projectDir, final String moduleName, final String version,
            final List<VersionedDependency> dependencies) {
        this.projectDir = projectDir;
        this.moduleName = moduleName;
        this.version = version;
        this.dependencies = dependencies;
    }

    PreviousRelease previousRelease() {
        return new PreviousRelease(this.projectDir, this.version);
    }

    Path getProjectDir() {
        return this.projectDir;
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

//    JsonObject getContent() {
//        return this.content;
//    }
}
