package com.exasol.projectkeeper.sources.analyze.generic;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

// Windows has problems with the timeout of 20ms. Running these tests under Unix is enough.
@DisabledOnOs(OS.WINDOWS)
class SimpleProcessIT {

    private static final Duration TIMEOUT = Duration.ofMillis(40);

    @Test
    void outputStream() {
        final SimpleProcess process = SimpleProcess.start(List.of("bash", "-c", "echo output"));
        process.waitUntilFinished(TIMEOUT);
        assertThat(process.getOutputStreamContent(), equalTo("output\n"));
        assertThat(process.getErrorStreamContent(), emptyString());
    }

    @Test
    void errorStream() {
        final SimpleProcess process = SimpleProcess.start(List.of("bash", "-c", ">&2 echo error"));
        process.waitUntilFinished(TIMEOUT);
        assertThat(process.getOutputStreamContent(), emptyString());
        assertThat(process.getErrorStreamContent(), equalTo("error\n"));
    }

    @Test
    void outputAndErrorStream() {
        final SimpleProcess process = SimpleProcess.start(List.of("bash", "-c", "echo output && >&2 echo error"));
        process.waitUntilFinished(TIMEOUT);
        assertThat(process.getOutputStreamContent(), equalTo("output\n"));
        assertThat(process.getErrorStreamContent(), equalTo("error\n"));
    }

    @Test
    void processFails() {
        final SimpleProcess process = SimpleProcess
                .start(List.of("bash", "-c", "echo output && >&2 echo error && exit 1"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> process.waitUntilFinished(TIMEOUT));
        assertThat(exception.getMessage(), allOf(startsWith(
                "E-PK-CORE-126: Failed to run command 'bash -c echo output && >&2 echo error && exit 1' in <null>, exit code was 1 after PT"),
                endsWith("Output:\n'output'\n" + //
                        "Error output:\n'error'")));
    }

    @Test
    void processTimeout() {
        final List<String> commandParts = List.of("bash", "-c", "echo output && >&2 echo error && sleep 1");
        final SimpleProcess process = SimpleProcess.start(commandParts);
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> process.waitUntilFinished(TIMEOUT));
        final String command = commandParts.stream().collect(joining(" "));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-128: Timeout while waiting " + TIMEOUT.toMillis()
                + "ms for command '" + command + "'. Output was 'output'\n" + "Error output: 'error'"));
    }

    @Test
    void executeFails() {
        final List<String> command = List.of("no-such-binary");
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> SimpleProcess.start(command));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-125: Error executing command 'no-such-binary'. Verify that the 'no-such-binary' executable is on the PATH."));
    }
}
