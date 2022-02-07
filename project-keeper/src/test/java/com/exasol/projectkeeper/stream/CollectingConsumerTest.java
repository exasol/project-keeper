package com.exasol.projectkeeper.stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectingConsumerTest {

    private static final Duration TIMEOUT = Duration.ofMillis(1);
    private CollectingConsumer collectingConsumer;

    @BeforeEach
    void setup() {
        collectingConsumer = new CollectingConsumer();
    }

    @Test
    void getEmptyContentReturnsEmptyString() throws InterruptedException {
        collectingConsumer.readFinished();
        assertThat(getcontent(), equalTo(""));
    }

    @Test
    void getSingleLineContent() throws InterruptedException {
        collectingConsumer.accept("line1");
        collectingConsumer.readFinished();
        assertThat(getcontent(), equalTo("line1\n"));
    }

    @Test
    void getMultiLineContent() throws InterruptedException {
        collectingConsumer.accept("line1");
        collectingConsumer.accept("line2");
        collectingConsumer.accept("line3");
        collectingConsumer.readFinished();
        assertThat(getcontent(), equalTo("line1\nline2\nline3\n"));
    }

    @Test
    void getContentAfterFailure() throws InterruptedException {
        collectingConsumer.accept("line1");
        collectingConsumer.readFailed(null);
        assertThat(getcontent(), equalTo("line1\n"));
    }

    @Test
    void getContentWithoutFinishFails() throws InterruptedException {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> getcontent());
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-99: Stream reading did not finish after timeout of "
                + TIMEOUT
                + " This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    private String getcontent() throws InterruptedException {
        return collectingConsumer.getContent(TIMEOUT);
    }
}
