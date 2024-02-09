package com.exasol.projectkeeper.sources.analyze.generic;

import java.io.IOException;
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
 * This is a convenient wrapper for {@link ProcessBuilder} and {@link Process} that simplifies starting a process,
 * waiting for it to finish and getting its stdout.
 */
public class SimpleProcess {
    private static final Logger LOGGER = Logger.getLogger(SimpleProcess.class.getName());

    private final Process process;
    private final CollectingConsumer outputStreamConsumer;
    private final CollectingConsumer errorStreamConsumer;
    private final Path workingDirectory;
    private final List<String> command;
    private final Instant startTime;

    SimpleProcess(final Process process, final CollectingConsumer outputStreamConsumer,
            final CollectingConsumer errorStreamConsumer, final Path workingDirectory, final List<String> command,
            final Instant startTime) {
        this.process = process;
        this.outputStreamConsumer = outputStreamConsumer;
        this.errorStreamConsumer = errorStreamConsumer;
        this.workingDirectory = workingDirectory;
        this.command = command;
        this.startTime = startTime;
    }

    /**
     * Starts a new process using the working directory of the current Java process.
     *
     * @param command the command to execute
     * @return a new {@link SimpleProcess} you can use to wait for the process to finish and retrieve its output
     */
    public static SimpleProcess start(final List<String> command) {
        return start(null, command);
    }

    /**
     * Starts a new process.
     *
     * @param workingDirectory the directory in which to start the process. Use the working directory of the current
     *                         Java process if {@code null}.
     * @param command          the command to execute
     * @return a new {@link SimpleProcess} you can use to wait for the process to finish and retrieve its output
     * @throws IllegalStateException if execution fails
     */
    public static SimpleProcess start(final Path workingDirectory, final List<String> command)
            throws IllegalStateException {
        LOGGER.finest(() -> "Executing command '" + formatCommand(command) + "' in working dir '" + workingDirectory
                + "'...");
        try {
            final Process process = new ProcessBuilder(command)
                    .directory(workingDirectory == null ? null : workingDirectory.toFile()) //
                    .redirectErrorStream(false) //
                    .start();
            final Instant startTime = Instant.now();
            final CollectingConsumer outputStreamConsumer = new AsyncStreamReader()
                    .startCollectingConsumer(process.getInputStream());
            final CollectingConsumer errorStreamConsumer = new AsyncStreamReader()
                    .startCollectingConsumer(process.getErrorStream());
            return new SimpleProcess(process, outputStreamConsumer, errorStreamConsumer, workingDirectory, command,
                    startTime);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-125")
                    .message("Error executing command {{command}}.", String.join(" ", command))
                    .mitigation("Verify that the {{executable name}} executable is on the PATH.", command.get(0))
                    .toString(), exception);
        }
    }

    /**
     * Wait for the process to finish.
     *
     * @param executionTimeout the maximum time to wait until the process finishes
     * @throws IllegalStateException if the process did not finish within the given timeout
     */
    public void waitUntilFinished(final Duration executionTimeout) {
        waitForExecutionFinished(executionTimeout);
        final Duration duration = Duration.between(this.startTime, Instant.now());
        final int exitCode = this.process.exitValue();
        if (exitCode != 0) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-126")
                    .message("Failed to run command {{executed command}} in {{working directory}}," //
                            + " exit code was {{exit code}} after {{duration}}." //
                            + " Output:\n{{std out}}\n" //
                            + "Error output:\n{{std error}}", //
                            formatCommand(), this.workingDirectory, exitCode, duration, //
                            getOutputStreamContent().trim(), getErrorStreamContent().trim())
                    .toString());
        }
        LOGGER.finest(() -> "Command '" + formatCommand() + "' finished successfully after " + duration);
    }

    /**
     * Get the standard output of the process.
     *
     * @return the standard output of the process
     */
    public String getOutputStreamContent() {
        try {
            return this.outputStreamConsumer.getContent(Duration.ofSeconds(5));
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    /**
     * Get the error output of the process.
     *
     * @return the error output of the process
     */
    public String getErrorStreamContent() {
        try {
            return this.errorStreamConsumer.getContent(Duration.ofSeconds(5));
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    /**
     * Get the process result containing output and error stream content.
     * 
     * @return result
     */
    public ProcessResult getResult() {
        return new ProcessResult(getOutputStreamContent(), getErrorStreamContent());
    }

    private void waitForExecutionFinished(final Duration executionTimeout) {
        try {
            if (!this.process.waitFor(executionTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                final String outputStreamContentUntilNow = this.outputStreamConsumer.getCurrentContent().trim();
                final String errorStreamContentUntilNow = this.errorStreamConsumer.getCurrentContent().trim();
                throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-128")
                        .message("Timeout while waiting {{timeout|u}}ms for command {{executed command}}." //
                                + " Output was {{std output}}\nError output: {{std error}}", //
                                executionTimeout.toMillis(), formatCommand(), //
                                outputStreamContentUntilNow, errorStreamContentUntilNow)
                        .toString());
            }
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    private RuntimeException handleInterruptedException(final InterruptedException exception) {
        Thread.currentThread().interrupt();
        return new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-129")
                .message("Interrupted while waiting for command {{executed command|u}}", formatCommand()).toString(),
                exception);
    }

    private static String formatCommand(final List<String> command) {
        return String.join(" ", command);
    }

    private String formatCommand() {
        return formatCommand(this.command);
    }
}
