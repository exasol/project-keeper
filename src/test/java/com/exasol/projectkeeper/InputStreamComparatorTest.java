package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

class InputStreamComparatorTest {

    private static final InputStreamComparator COMPARATOR = new InputStreamComparator();

    @Test
    void testEqual() throws IOException {
        try (final InputStream stream1 = new ByteArrayInputStream("test".getBytes());
                final InputStream stream2 = new ByteArrayInputStream("test".getBytes())) {
            assertThat(COMPARATOR.areStreamsEqual(stream1, stream2), equalTo(true));
        }
    }

    @Test
    void testNotEqual() throws IOException {
        try (final InputStream stream1 = new ByteArrayInputStream("test".getBytes());
                final InputStream stream2 = new ByteArrayInputStream("other".getBytes())) {
            assertThat(COMPARATOR.areStreamsEqual(stream1, stream2), equalTo(false));
        }
    }

    @Test
    void testNotEqualButSameLength() throws IOException {
        try (final InputStream stream1 = new ByteArrayInputStream("test1".getBytes());
                final InputStream stream2 = new ByteArrayInputStream("test2".getBytes())) {
            assertThat(COMPARATOR.areStreamsEqual(stream1, stream2), equalTo(false));
        }
    }
}