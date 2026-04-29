package com.exasol.projectkeeper.sources.analyze.npm;

import static com.exasol.projectkeeper.sources.analyze.npm.TestData.json;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class NpmLicenseTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(NpmLicense.class).verify();
    }

    @Test
    void convertLicensesWithDuplicateEntry() {
        final var result = NpmLicense.from(
                json("""
                        {
                            "string-length@4.0.2": {
                              "licenses": "MIT",
                              "repository": "https://github.com/sindresorhus/string-length"
                            },
                            "string-width@4.2.3": {
                              "licenses": "MIT",
                              "repository": "https://github.com/sindresorhus/string-width"
                            },
                            "string-width@5.1.2": {
                              "licenses": "MIT",
                              "repository": "https://github.com/sindresorhus/string-width"
                            }
                        }
                        """));
        assertAll(
                () -> assertThat(result, aMapWithSize(2)),
                () -> assertThat(result, hasEntry("string-width", List.of(
                        new NpmLicense("string-width", "4.2.3", "MIT", "https://github.com/sindresorhus/string-width"),
                        new NpmLicense("string-width", "5.1.2", "MIT",
                                "https://github.com/sindresorhus/string-width")))),
                () -> assertThat(result, hasEntry("string-length", List
                        .of(new NpmLicense("string-length", "4.0.2", "MIT",
                                "https://github.com/sindresorhus/string-length")))));
    }

    @Test
    void convertLicensesWithLicenseArray() {
        final var result = NpmLicense.from(
                json("""
                        {
                            "pause-stream@0.0.11": {
                              "licenses": ["MIT", "Apache2"],
                              "repository": "https://github.com/isaacs/pause-stream"
                            }
                        }
                        """));
        assertAll(
                () -> assertThat(result, aMapWithSize(1)),
                () -> assertThat(result, hasEntry("pause-stream", List.of(
                        new NpmLicense("pause-stream", "0.0.11", "MIT", "https://github.com/isaacs/pause-stream"),
                        new NpmLicense("pause-stream", "0.0.11", "Apache2",
                                "https://github.com/isaacs/pause-stream")))));
    }

    @Test
    void convertLicensesWithEmptyLicenseArray() {
        final var result = NpmLicense.from(
                json("""
                        {
                            "pause-stream@0.0.11": {
                              "licenses": [],
                              "repository": "https://github.com/isaacs/pause-stream"
                            }
                        }
                        """));
        assertAll(
                () -> assertThat(result, aMapWithSize(1)),
                () -> assertThat(result, hasEntry("pause-stream", List.of())));
    }
}
