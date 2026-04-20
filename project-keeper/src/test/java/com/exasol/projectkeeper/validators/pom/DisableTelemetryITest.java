package com.exasol.projectkeeper.validators.pom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class DisableTelemetryITest {
    @Test
    // [itest->dsn~disable-telemetry.integration-tests~1]
    void testTelemetryDisabledForFailsafeIntegrationTests() {
        assertThat(System.getenv("EXASOL_TELEMETRY_DISABLE"), equalTo("true"));
    }
}
