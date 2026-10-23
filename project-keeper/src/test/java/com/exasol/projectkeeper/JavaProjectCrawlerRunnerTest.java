package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.ClearSystemProperty;
import org.junitpioneer.jupiter.RestoreSystemProperties;
import org.junitpioneer.jupiter.SetSystemProperty;

class JavaProjectCrawlerRunnerTest {

    @Test
    @ClearSystemProperty(key = JavaProjectCrawlerRunner.TIMEOUT_PROPERTY)
    void timeoutDefaultsTo120Seconds() {
        assertThat(JavaProjectCrawlerRunner.readTimeout(), equalTo(Duration.ofSeconds(120)));
    }

    @Test
    @SetSystemProperty(key = JavaProjectCrawlerRunner.TIMEOUT_PROPERTY, value = " ")
    void timeoutUsesDefaultForBlankProperty() {
        assertThat(JavaProjectCrawlerRunner.readTimeout(), equalTo(JavaProjectCrawlerRunner.DEFAULT_TIMEOUT));
    }

    @ParameterizedTest
    @ValueSource(longs = { 1, 180, 1234567890 })
    @RestoreSystemProperties
    void timeoutReadFromSystemProperty(final long seconds) {
        System.setProperty(JavaProjectCrawlerRunner.TIMEOUT_PROPERTY, String.valueOf(seconds));
        assertThat(JavaProjectCrawlerRunner.readTimeout(), equalTo(Duration.ofSeconds(seconds)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "0", "-5", "abc", "1.5" })
    @RestoreSystemProperties
    void timeoutRejectsInvalidValue(final String value) {
        System.setProperty(JavaProjectCrawlerRunner.TIMEOUT_PROPERTY, value);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                JavaProjectCrawlerRunner::readTimeout);
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-216: Invalid value '" + value
                + "' for system property 'project-keeper.crawlerTimeout'."
                + " Specify the crawler timeout as a positive number of seconds, e.g. '-Dproject-keeper.crawlerTimeout=180'."));
    }
}
