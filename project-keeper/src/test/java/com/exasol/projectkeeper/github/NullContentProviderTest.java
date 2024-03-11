package com.exasol.projectkeeper.github;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.github.NullContentProvider;

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
