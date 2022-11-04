package com.exasol.projectkeeper.sources.analyze.npm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;

class PackageJsonReaderTest {
    private PackageJsonReader reader;

    @BeforeEach
    void setup() {
        this.reader = new PackageJsonReader();
    }

    @Test
    void missingNameFails() {
        final JsonObject object = Json.createObjectBuilder().build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.read(null, object));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-163: Missing attribute 'name' in package.json. Add a 'name' attribute."));
    }

    @Test
    void missingVersionFails() {
        final JsonObject object = Json.createObjectBuilder().add("name", "dummy").build();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.read(null, object));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-164: Missing attribute 'version' in package.json. Add a 'version' attribute."));
    }

    @Test
    void readingSucceedsWhenNameAndVersionPresent() {
        final JsonObject object = Json.createObjectBuilder().add("name", "moduleName").add("version", "moduleVersion")
                .build();
        final PackageJson pkg = this.reader.read(null, object);
        assertAll(() -> assertThat(pkg.getModuleName(), equalTo("moduleName")),
                () -> assertThat(pkg.getVersion(), equalTo("moduleVersion")));
    }
}
