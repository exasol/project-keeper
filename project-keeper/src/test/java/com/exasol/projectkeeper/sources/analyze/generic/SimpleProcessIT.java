package com.exasol.projectkeeper.sources.analyze.generic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;

class SimpleProcessIT {

    private static final Duration TIMEOUT = Duration.ofMillis(20);

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
        final SimpleProcess process = SimpleProcess
                .start(List.of("bash", "-c", "echo output && >&2 echo error && sleep 1"));
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> process.waitUntilFinished(TIMEOUT));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-128: Timeout while waiting 10ms for command 'bash -c echo output && >&2 echo error && sleep 1'. Output was 'output'\n"
                        + "Error output: 'error'"));
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
