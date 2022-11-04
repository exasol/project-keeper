package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

import jakarta.json.*;

final class PackageJsonReader {

    enum DependencyKey {
        COMPILE("dependencies", Type.COMPILE), //
        DEV("devDependencies", Type.DEV);

        private final String key;
        private final Type type;

        private DependencyKey(final String key, final Type type) {
            this.key = key;
            this.type = type;
        }
    }

    static PackageJson read(final Path path) {
        return new PackageJsonReader().read(path, JsonIo.uncheckedRead(path));
    }

    static PackageJson read(final Path path, final String string) {
        final JsonObject json = JsonIo.read(new StringReader(string));
        return new PackageJsonReader().read(path, json);
    }

    PackageJson read(final Path path, final JsonObject content) {
        final String module = retrieveModuleName(content);
        final String version = retrieveModuleVersion(content);
        final List<VersionedDependency> dependencies = new ArrayList<>();
        dependencies.addAll(versionedDependencies(content, DependencyKey.DEV));
        dependencies.addAll(versionedDependencies(content, DependencyKey.COMPILE));
        return new PackageJson(path, module, version, dependencies);
    }

    private static final String MODULE_PREFIX = "@exasol/";

    private String retrieveModuleName(final JsonObject packageJson) {
        final JsonString raw = packageJson.getJsonString("name");
        if (raw == null) {
            throw new IllegalArgumentException(
                    ExaError.messageBuilder("E-PK-CORE-163").message("Missing attribute 'name' in package.json.")
                            .mitigation("Add a 'name' attribute.").toString());
        }
        final String rawString = raw.getString();
        if (!rawString.startsWith(MODULE_PREFIX)) {
            return rawString;
        }
        return rawString.substring(MODULE_PREFIX.length());
    }

    private String retrieveModuleVersion(final JsonObject content) {
        final JsonString version = content.getJsonString("version");
        if (version == null) {
            throw new IllegalArgumentException(
                    ExaError.messageBuilder("E-PK-CORE-164").message("Missing attribute 'version' in package.json.")
                            .mitigation("Add a 'version' attribute.").toString());
        }
        return version.getString();
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