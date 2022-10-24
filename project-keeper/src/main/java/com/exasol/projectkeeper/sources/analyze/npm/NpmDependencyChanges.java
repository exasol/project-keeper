package com.exasol.projectkeeper.sources.analyze.npm;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChange;
import com.exasol.projectkeeper.shared.dependencychanges.DependencyChangeReport;
import com.exasol.projectkeeper.sources.analyze.generic.DependencyChanges;

/**
 * Services for handling NPM modules
 */
public class NpmDependencyChanges {

    private final PackageJson current;

    NpmDependencyChanges(final PackageJson packageJson) {
        this.current = packageJson;
    }

    DependencyChangeReport getReport() {
//        final JsonObject current = this.current.getContent();
        final Optional<PackageJson> previous = this.current.previousRelease() //
                .fileContent(Paths.get(PackageJsonReader.FILENAME)) //
                .map(PackageJsonReader::readFull);
        return DependencyChangeReport.builder() //
                .typed(Type.COMPILE, getChanges(previous, Type.COMPILE)) //
                .typed(Type.PLUGIN, getChanges(previous, Type.PLUGIN)) //
                .build();
    }

    private List<DependencyChange> getChanges(final Optional<PackageJson> previous, final Type type) {
        return DependencyChanges.builder() //
                .from(previous.map(p -> p.getDependencies(type))) //
                .to(this.current.getDependencies(type)) //
                .build();
    }

//    private List<DependencyChange> versionedDependencies(final Optional<JsonObject> previous, final PackageJson current,
//            final String key) {
//        return DependencyChanges.builder() //
//                .from(previous.map(p -> versionedDependencies(p, key))) //
//                .to(versionedDependencies(current, key)) //
//                .build();
//    }

//    private List<VersionedDependency> versionedDependencies(final JsonObject packageJson, final String key) {
//        final JsonObject dependencies = orEmpty(packageJson.getJsonObject(key));
//        return dependencies.keySet().stream() //
//                .map(k -> change(k, dependencies.getString(k))) //
//                .collect(Collectors.toList());
//    }

//    private JsonObject orEmpty(final JsonObject jsonObject) {
//        return jsonObject != null ? jsonObject : JsonValue.EMPTY_JSON_OBJECT;
//    }

//    private VersionedDependency change(final String name, final String version) {
//        return VersionedDependency.builder().name(name).version(version).isIndirect(false).build();
//    }
}
