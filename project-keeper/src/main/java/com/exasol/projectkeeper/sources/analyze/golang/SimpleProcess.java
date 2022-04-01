package com.exasol.projectkeeper.sources.analyze.golang;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.stream.AsyncStreamReader;
import com.exasol.projectkeeper.stream.CollectingConsumer;

/**
 * This is a convenient wrapper for {@link ProcessBuilder} and {@link Process} that simplifies waiting for a process and
 * getting its stdout.
 */
public class SimpleProcess {
    private static final Logger LOGGER = Logger.getLogger(SimpleProcess.class.getName());

    private final Process process;
    private final CollectingConsumer streamConsumer;
    private final List<String> command;

    private final Instant startTime;

    private SimpleProcess(final Process process, final CollectingConsumer streamConsumer, final List<String> command,
            final Instant startTime) {
        this.process = process;
        this.streamConsumer = streamConsumer;
        this.command = command;
        this.startTime = startTime;
    }

    public static SimpleProcess start(final Path workingDirectory, final List<String> command) {
        LOGGER.fine(() -> "Executing command '" + formatCommand(command) + "' in working dir " + workingDirectory);
        try {
            final Process process = new ProcessBuilder(command)
                    .directory(workingDirectory == null ? null : workingDirectory.toFile()) //
                    .redirectErrorStream(false) //
                    .start();
            final Instant startTime = Instant.now();
            final CollectingConsumer streamConsumer = new AsyncStreamReader()
                    .startCollectingConsumer(process.getInputStream());
            return new SimpleProcess(process, streamConsumer, command, startTime);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-125")
                    .message("Error executing command {{command}}.", String.join(" ", command))
                    .mitigation("Verify that the {{executable name}} executable is on the PATH.", command.get(0))
                    .toString(), exception);
        }
    }

    public String getOutput(final Duration executionTimeout) {
        waitForExecutionFinished(executionTimeout);
        final Duration duration = Duration.between(this.startTime, Instant.now());
        final int exitCode = this.process.exitValue();
        final String output = getStreamOutput(executionTimeout);
        if (exitCode != 0) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-126").message(
                    "Failed to run command {{executed command}}, exit code was {{exit code}} after {{duration}}. Output:\n{{std out}}\nError output:\n{{std error}}",
                    formatCommand(), exitCode, duration, output, getStdError()).toString());
        }
        LOGGER.fine(() -> "Command '" + formatCommand() + "' finished successfully after " + duration + ", output: '"
                + output + "'");
        return output;
    }

    private String getStdError() {
        try (InputStream errorStream = this.process.getErrorStream()) {
            return new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-PK-CORE-127")
                    .message("Failed to read error stream from command {{command}}", formatCommand()).toString(),
                    exception);
        }
    }

    private String getStreamOutput(final Duration executionTimeout) {
        try {
            return this.streamConsumer.getContent(executionTimeout);
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    private void waitForExecutionFinished(final Duration executionTimeout) {
        try {
            if (!this.process.waitFor(executionTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                final String output = getOutput(executionTimeout);
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-128").message(
                        "Timeout while waiting {{timeout|uq}}ms for command {{executed command}}. Output was {{output}}\nError output: {{std error}}",
                        executionTimeout.toMillis(), formatCommand(), output, getStdError()).toString());
            }
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    private RuntimeException handleInterruptedException(final InterruptedException exception) {
        Thread.currentThread().interrupt();
        return new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-129")
                .message("Interrupted while waiting for command {{executed command|uq}}", formatCommand()).toString(),
                exception);
    }

    private static String formatCommand(final List<String> command) {
        return String.join(" ", command);
    }

    private String formatCommand() {
        return formatCommand(this.command);
    }
}
