package com.exasol.projectkeeper.validators.pom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class DisableTelemetryTest {
    @Test
    // [utest->dsn~disable-telemetry.unit-tests~1]
    void testTelemetryDisabledForSurefireUnitTests() {
        assertThat(System.getenv("EXASOL_TELEMETRY_DISABLE"), equalTo("true"));
    }
}
