package com.exasol.projectkeeper.cli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProjectKeeperLauncherIT {
    @TempDir
    Path projectDir;

    @BeforeEach
    void setup() {
    }

    private static Arguments args(final String... args) {
        return Arguments.of(new Object[] { args });
    }

    static Stream<Arguments> invalidArguments() {
        return Stream.of(//
                Arguments.of(new Object[] { null }), //
                args(), //
                args("fix", "verify"), //
                args("unknown") //
        );
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    void throwsExceptionForWrongArguments(final String... args) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> run(args));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-140: Got no or invalid command line argument '"
                + Arrays.toString(args) + "'. Please only specify arguments 'verify' or 'fix'."));
    }

    @Test
    void verifyFailsForEmptyProject() throws IOException, GitAPIException {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> runWithCurrentWorkingDir("verify"));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-141: Failed to run project keeper 'verify' See log messages above for details."));
    }

    @Test
    void fixingSucceedsForEmptyProject() throws IOException, GitAPIException {
        assertDoesNotThrow(() -> runWithCurrentWorkingDir("fix"));
    }

    @Test
    void verifySucceedsAfterFixing() throws IOException, GitAPIException {
        runWithCurrentWorkingDir("fix");
        assertDoesNotThrow(() -> runWithCurrentWorkingDir("verify"));
    }

    private void runWithCurrentWorkingDir(final String... args) {
        new ProjectKeeperLauncher(this.projectDir).start(args);
    }

    private void run(final String... args) {
        ProjectKeeperLauncher.main(args);
    }
}
