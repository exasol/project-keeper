package com.exasol.projectkeeper.githuboutput;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class NullOutputPublisherTest {
    @Test
    void publish() {
        assertDoesNotThrow(() -> new NullOutputPublisher().publish("key", "value"));
    }

    @Test
    void close() {
        assertDoesNotThrow(new NullOutputPublisher()::close);
    }
}
