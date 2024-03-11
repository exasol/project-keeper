package com.exasol.projectkeeper.githuboutput;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class NullContentProviderTest {
    @Test
    void publish() {
        assertDoesNotThrow(() -> new NullContentProvider().publish("key", "value"));
    }

    @Test
    void close() {
        assertDoesNotThrow(new NullContentProvider()::close);
    }
}
