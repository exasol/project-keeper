package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.ByteArrayInputStream;

import com.exasol.projectkeeper.AsyncStreamReader.CollectingConsumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsyncStreamReaderTest {

    private AsyncStreamReader asyncStreamReader;

    @BeforeEach
    void setup() {
        asyncStreamReader = new AsyncStreamReader(Runnable::run);
    }

    @Test
    void testEmptyStream() {
        CollectingConsumer consumer = asyncStreamReader.startCollectingConsumer(new ByteArrayInputStream(new byte[0]));
        assertThat(consumer.toString(), equalTo(""));
    }

    @Test
    void testStreamWithUnixNewlines() {
        String content = "Multi line\nStreamContent";
        CollectingConsumer consumer = asyncStreamReader
                .startCollectingConsumer(new ByteArrayInputStream(content.getBytes()));
        assertThat(consumer.toString(), equalTo("Multi line\nStreamContent\n"));
    }

    @Test
    void testStreamWithWindowsNewlines() {
        String content = "Multi line\r\nStreamContent";
        CollectingConsumer consumer = asyncStreamReader
                .startCollectingConsumer(new ByteArrayInputStream(content.getBytes()));
        assertThat(consumer.toString(), equalTo("Multi line\nStreamContent\n"));
    }
}
