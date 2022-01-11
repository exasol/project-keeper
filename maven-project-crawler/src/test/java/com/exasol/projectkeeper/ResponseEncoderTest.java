package com.exasol.projectkeeper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ResponseEncoderTest {

    @ParameterizedTest
    @ValueSource(strings = { "some text###SerializedResponseStart###abc", "some text###SerializedResponseEnd###abc" })
    void testInjection(String input) {
        final ResponseEncoder responseEncoder = new ResponseEncoder();
        assertThrows(IllegalStateException.class, () -> responseEncoder.printResponse(input));
    }
}