package com.exasol.projectkeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This class starts a new {@link Thread} that reads from an {@link InputStream} and forwards the input line by line to
 * a given {@link Consumer}.
 */
class AsyncStreamReader implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(AsyncStreamReader.class.getName());

    private final InputStream stream;
    private final Consumer<String> consumer;

    private AsyncStreamReader(InputStream stream, Consumer<String> consumer) {
        this.stream = stream;
        this.consumer = consumer;
    }

    /**
     * Start a new {@link Thread} that reads from the given {@link InputStream} and forwards the input to a
     * {@link CollectingConsumer}.
     * 
     * @param stream the input stream to read
     * @return a {@link CollectingConsumer} that collects the data from the input stream
     */
    static CollectingConsumer startCollectingConsumer(InputStream stream) {
        CollectingConsumer consumer = new CollectingConsumer();
        start(stream, consumer);
        return consumer;
    }

    /**
     * Start a new {@link Thread} that reads from the given {@link InputStream} and forwards the input line by line to
     * the given {@link Consumer}.
     * 
     * @param stream   the input stream to read
     * @param consumer the consumer
     * @return the new reader
     */
    static AsyncStreamReader start(InputStream stream, Consumer<String> consumer) {
        AsyncStreamReader reader = new AsyncStreamReader(stream, consumer);
        Thread thread = new Thread(reader);
        thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
        thread.start();
        return reader;
    }

    @Override
    public void run() {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("E-PK-CORE-96").message("Failed to read input stream").toString(),
                    exception);
        }
    }

    static class CollectingConsumer implements Consumer<String> {
        private final StringBuilder stringBuilder = new StringBuilder();

        @Override
        public void accept(String line) {
            LOGGER.fine(() -> "OUTPUT> " + line);
            stringBuilder.append(line).append("\n");
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }

    private static class LoggingExceptionHandler implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            LOGGER.log(Level.SEVERE, ExaError.messageBuilder("E-PK-CORE-97").message("Failed to read input stream")
                    .ticketMitigation().toString(), throwable);
        }
    }
}