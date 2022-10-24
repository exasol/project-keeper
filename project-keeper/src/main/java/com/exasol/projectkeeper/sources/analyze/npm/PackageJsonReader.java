package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.BaseDependency.Type;
import com.exasol.projectkeeper.shared.dependencies.VersionedDependency;

import jakarta.json.*;

final class PackageJsonReader {
    /** name of file containing NPM package information */
    static final String FILENAME = "package.json";

    private static final String MODULE_PREFIX = "@exasol/";

    enum DependencyKey {
        COMPILE("dependencies", Type.COMPILE), //
        PLUGIN("devDependencies", Type.PLUGIN);

        private final String key;
        private final Type type;

        /**
         * @param key
         * @param type
         */
        private DependencyKey(final String key, final Type type) {
            this.key = key;
            this.type = type;
        }
    }

    static PackageJson read(final Path projectDir) {
        return new PackageJsonReader().read2(projectDir);
    }

    static JsonObject read(final String string) {
        return readFromReader(new StringReader(string));
    }

    static PackageJson readFull(final String string) {
        return new PackageJsonReader().read2(null, readFromReader(new StringReader(string)));
    }

    static JsonObject readFromReader(final Reader reader) {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }

    PackageJson read2(final Path projectDir) {
        final JsonObject content = uncheckedRead(projectDir.resolve(FILENAME));
        return read2(projectDir, content);
    }

    PackageJson read2(final Path projectDir, final JsonObject content) {
        final String module = retrieveModuleName(content);
        final String version = content.getString("version");
        final List<VersionedDependency> dependencies = new ArrayList<>();
        dependencies.addAll(versionedDependencies(content, DependencyKey.COMPILE));
        dependencies.addAll(versionedDependencies(content, DependencyKey.PLUGIN));
        return new PackageJson(projectDir, module, version, dependencies);
    }

    private JsonObject uncheckedRead(final Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            return readFromReader(reader);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

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