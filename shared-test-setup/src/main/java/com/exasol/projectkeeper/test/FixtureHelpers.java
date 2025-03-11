package com.exasol.projectkeeper.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class FixtureHelpers {
    private static final Logger LOG = Logger.getLogger(FixtureHelpers.class.getName());
    private static final Duration PROCESS_TIMEOUT = Duration.ofSeconds(120);

    static void execute(final Path workingDir, final String... command) {
        LOG.fine(() -> "Running command %s in %s...".formatted(Arrays.toString(command), workingDir));
        try {
            final Process process = new ProcessBuilder(command)
                    .directory(workingDir.toFile())
                    .redirectErrorStream(false)
                    .start();
            final boolean success = process.waitFor(PROCESS_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            if (!success) {
                final String stdOut = readStream(process.getInputStream());
                final String errorOut = readStream(process.getErrorStream());
                throw new AssertionError("Command " + Arrays.toString(command) + " did not finish after "
                        + PROCESS_TIMEOUT + ", std output: '" + stdOut + "', error output: '" + errorOut + "'.");
            }
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
        }
    }

    private static String readStream(final InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
