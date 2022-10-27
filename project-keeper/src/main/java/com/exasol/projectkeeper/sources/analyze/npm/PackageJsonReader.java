package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

final class PackageJsonReader {

    /** path of file containing NPM package information */
    static final Path PATH = Paths.get("package.json");

    enum DependencyKey {
        COMPILE("dependencies", Type.COMPILE), //
        PLUGIN("devDependencies", Type.PLUGIN);

        private final String key;
        private final Type type;

        private DependencyKey(final String key, final Type type) {
            this.key = key;
            this.type = type;
        }
    }

    static PackageJson read(final Path path) {
        final JsonObject json = JsonIo.uncheckedRead(path);
        return new PackageJsonReader().read(path, json);
    }

    static PackageJson read(final String string) {
        final JsonObject json = JsonIo.read(new StringReader(string));
        return new PackageJsonReader().read(null, json);
    }

    PackageJson read(final Path path, final JsonObject content) {
        final String module = retrieveModuleName(content);
        final String version = content.getString("version");
        final List<VersionedDependency> dependencies = new ArrayList<>();
        dependencies.addAll(versionedDependencies(content, DependencyKey.COMPILE));
        dependencies.addAll(versionedDependencies(content, DependencyKey.PLUGIN));
        return new PackageJson(path, module, version, dependencies);
    }

    private static final String MODULE_PREFIX = "@exasol/";

    private String retrieveModuleName(final JsonObject packageJson) {
        final String raw = packageJson.getString("name");
        if (!raw.startsWith(MODULE_PREFIX)) {
            return raw;
        }
        return raw.substring(MODULE_PREFIX.length());
    }

    private List<VersionedDependency> versionedDependencies(final JsonObject packageJson, final DependencyKey key) {
        final JsonObject dependencies = orEmpty(packageJson.getJsonObject(key.key));
        return dependencies.keySet().stream() //
                .map(k -> dependency(k, key.type, dependencies.getString(k))) //
                .collect(Collectors.toList());
    }

    private JsonObject orEmpty(final JsonObject jsonObject) {
        return jsonObject != null ? jsonObject : JsonValue.EMPTY_JSON_OBJECT;
    }

    private VersionedDependency dependency(final String name, final Type type, final String version) {
        return VersionedDependency.builder().type(type).name(name).version(version).isIndirect(false).build();
    }
}