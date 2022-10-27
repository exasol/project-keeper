package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.*;

import jakarta.json.JsonObject;

class JsonFixture {

    private JsonFixture() {
        // only static usage
    }

    static String jsonString(final String... s) {
        return String.join("\n", s).replace('\'', '"');
    }

    static JsonObject fromResource(final String resource) {
        try (InputStreamReader reader = new InputStreamReader(JsonFixture.class.getResourceAsStream(resource))) {
            return JsonIo.read(reader);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    static PackageJson currentPackageJson() {
        return new PackageJsonReader().read(null, fromResource("/npm/current-package.json"));
    }

    static PackageJson previousPackageJson() {
        return new PackageJsonReader().read(null, fromResource("/npm/previous-package.json"));
    }

    static PackageJson packageJson(final String resource) {
        return new PackageJsonReader().read(null, fromResource(resource));
    }

    static JsonObject dependenciesAdditionalInfos() {
        return fromResource("/npm/dependencies-additional-info.json");
    }

    static JsonObject licenceInfo() {
        return fromResource("/npm/licences.json");
    }
}
