package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.json.*;

class JsonIo {

    private JsonIo() {
        // only static usage
    }

    static JsonObject uncheckedRead(final Path file) {
        try (Reader reader = Files.newBufferedReader(file)) {
            return read(reader);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    static JsonObject read(final Reader reader) {
        try (JsonReader jsonReader = Json.createReader(reader)) {
            return jsonReader.readObject();
        }
    }
}
