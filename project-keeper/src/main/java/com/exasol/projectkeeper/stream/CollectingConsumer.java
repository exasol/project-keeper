package com.exasol.projectkeeper.stream;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This {@link StreamConsumer} collects all lines into a {@link StringBuilder} and provides a method for check if
 * reading has finished.
 */
public class CollectingConsumer implements StreamConsumer {
    private static final Logger LOGGER = Logger.getLogger(CollectingConsumer.class.getName());

    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public void accept(String line) {
        LOGGER.finest(() -> "OUTPUT> " + line);
        stringBuilder.append(line).append("\n");
    }

    @Override
    public void readFinished() {
        countDownLatch.countDown();
    }

    @Override
    public void readFailed(IOException ioException) {
        countDownLatch.countDown();
    }

    /**
     * Waits until the stream was read completely and returns the read content.
     * 
     * @param timeout the maximum time to wait
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public String getContent(Duration timeout) throws InterruptedException {
        boolean result = countDownLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (!result) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-99")
                    .message("Stream reading did not finish after timeout of {{timeout}}", timeout).ticketMitigation()
                    .toString());
        }
        return stringBuilder.toString();
    }
}