package com.exasol.projectkeeper.shared.mavenprojectcrawler;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.itsallcode.io.Capturable;
import org.itsallcode.junit.sysextensions.SystemOutGuard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@ExtendWith(SystemOutGuard.class)
class ResponseCoderTest {

    @ParameterizedTest
    @ValueSource(strings = { "some text###SerializedResponseStart###abc", "some text###SerializedResponseEnd###abc" })
    void testInjection(final String input) {
        final ResponseCoder responseEncoder = new ResponseCoder();
        assertThrows(IllegalStateException.class, () -> responseEncoder.printResponse(input));
    }

    @Test
    void testEncodeDecode(final Capturable stream) {
        stream.capture();
        final String testString = "hallo 123\n 123";
        new ResponseCoder().printResponse(testString);
        final String result = stream.getCapturedData();
        final String decoded = new ResponseCoder().decodeResponse(result);
        assertThat(decoded, equalTo(testString));
    }

    @Test
    void testInvalidResponse() {
        final ResponseCoder responseCoder = new ResponseCoder();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> responseCoder.decodeResponse("123"));
        assertThat(exception.getMessage(), equalTo(
                "F-PK-SMC-79: Invalid response from crawler plugin: '123'. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }
}