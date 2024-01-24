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
        this.collectingConsumer = new CollectingConsumer();
    }

    @Test
    void getEmptyContentReturnsEmptyString() throws InterruptedException {
        this.collectingConsumer.readFinished();
        assertThat(getContent(), equalTo(""));
    }

    @Test
    void getSingleLineContent() throws InterruptedException {
        this.collectingConsumer.accept("line1");
        this.collectingConsumer.readFinished();
        assertThat(getContent(), equalTo("line1\n"));
    }

    @Test
    void getMultiLineContent() throws InterruptedException {
        this.collectingConsumer.accept("line1");
        this.collectingConsumer.accept("line2");
        this.collectingConsumer.accept("line3");
        this.collectingConsumer.readFinished();
        assertThat(getContent(), equalTo("line1\nline2\nline3\n"));
    }

    @Test
    void getContentAfterFailure() throws InterruptedException {
        this.collectingConsumer.accept("line1");
        this.collectingConsumer.readFailed(null);
        assertThat(getContent(), equalTo("line1\n"));
    }

    @Test
    void getContentWithoutFinishFails() throws InterruptedException {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> getContent());
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-99: Stream reading did not finish after timeout of "
                + TIMEOUT
                + ". Content collected until now: ''. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    @Test
    void getContentWithoutFinishAddsCollectedContentToExceptionMessage() throws InterruptedException {
        this.collectingConsumer.accept("line1");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> getContent());
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-99: Stream reading did not finish after timeout of "
                + TIMEOUT
                + ". Content collected until now: 'line1\n'. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    private String getContent() throws InterruptedException {
        return this.collectingConsumer.getContent(TIMEOUT);
    }
}
