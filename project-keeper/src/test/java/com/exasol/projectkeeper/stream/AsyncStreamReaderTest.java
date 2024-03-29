package com.exasol.projectkeeper.stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.ByteArrayInputStream;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AsyncStreamReaderTest {

    private AsyncStreamReader asyncStreamReader;

    @BeforeEach
    void setup() {
        asyncStreamReader = new AsyncStreamReader(Runnable::run);
    }

    @Test
    void testEmptyStream() throws InterruptedException {
        CollectingConsumer consumer = asyncStreamReader.startCollectingConsumer(new ByteArrayInputStream(new byte[0]));
        assertThat(consumer.getContent(Duration.ofMillis(1)), equalTo(""));
    }

    @Test
    void testStreamWithUnixNewlines() throws InterruptedException {
        String content = "Multi line\nStreamContent";
        CollectingConsumer consumer = asyncStreamReader
                .startCollectingConsumer(new ByteArrayInputStream(content.getBytes()));
        assertThat(consumer.getContent(Duration.ofMillis(1)), equalTo("Multi line\nStreamContent\n"));
    }

    @Test
    void testStreamWithWindowsNewlines() throws InterruptedException {
        String content = "Multi line\r\nStreamContent";
        CollectingConsumer consumer = asyncStreamReader
                .startCollectingConsumer(new ByteArrayInputStream(content.getBytes()));
        assertThat(consumer.getContent(Duration.ofMillis(1)), equalTo("Multi line\nStreamContent\n"));
    }
}
