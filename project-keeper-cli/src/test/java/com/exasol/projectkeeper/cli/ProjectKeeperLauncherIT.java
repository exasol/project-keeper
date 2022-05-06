package com.exasol.projectkeeper.cli;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

class ProjectKeeperLauncherIT {
    private static final Path PARENT_POM = Path.of("../parent-pom/pom.xml");
    public static final String CURRENT_VERSION = MavenProjectVersionGetter.getProjectRevision(PARENT_POM);

    @TempDir
    Path projectDir;

    @BeforeEach
    void setup() {
    }

    private static Arguments args(final String... args) {
        return Arguments.of(new Object[] { args });
    }

    static Stream<Arguments> invalidArguments() {
        return Stream.of( //
                args(), //
                args("fix", "verify"), //
                args("unknown") //
        );
    }

    @ParameterizedTest
    @MethodSource("invalidArguments")
    void failsForWrongArguments(final String... args) throws IOException, InterruptedException {
        final Process process = run(this.projectDir, args);
        assertProcessFails(process, 1, "E-PK-CORE-140: Got no or invalid command line argument");
    }

    private void assertProcessSucceeds(final Process process, final String expectedMessage)
            throws InterruptedException, IOException {
        final int exitCode = process.waitFor();
        final String stdOut = readString(process.getInputStream());
        final String stdErr = readString(process.getErrorStream());
        assertAll(() -> assertThat(exitCode, equalTo(0)), //
                () -> assertThat(stdOut, containsString(expectedMessage)), //
                () -> assertThat(stdErr, equalTo("")));
    }

    private void assertProcessFails(final Process process, final int expectedExitCode,
            final String expectedErrorMessage) throws InterruptedException, IOException {
        final int exitCode = process.waitFor();
        final String stdOut = readString(process.getInputStream());
        final String stdErr = readString(process.getErrorStream());
        assertAll(() -> assertThat(exitCode, equalTo(expectedExitCode)), //
                () -> assertThat(stdOut, equalTo("")), //
                () -> assertThat(stdErr, containsString(expectedErrorMessage)));
    }

    private String readString(final InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }

    @Test
    void verifyFailsForEmptyProject() throws IOException, InterruptedException {
        final Process process = run(this.projectDir, "verify");
        assertProcessFails(process, 1, "asdf");
    }

    @Test
    void fixingSucceedsForEmptyProject() throws IOException, InterruptedException {
        final Process process = run(this.projectDir, "fix");
        assertProcessSucceeds(process, "asdf");
    }

    @Test
    void verifySucceedsAfterFixing() throws IOException {
        runWithCurrentWorkingDir("fix");
        assertDoesNotThrow(() -> runWithCurrentWorkingDir("verify"));
    }

    private void runWithCurrentWorkingDir(final String... args) {
        new ProjectKeeperLauncher(this.projectDir).start(args);
    }

    private Process run(final Path workingDir, final String... args) throws IOException {
        final Path jar = Paths.get("target/project-keeper-" + "cli-" + CURRENT_VERSION + ".jar").toAbsolutePath();
        if (!Files.exists(jar)) {
            fail("Jar " + jar + " not found. Run 'mvn package' to build it.");
        }
        final List<String> commandLine = new ArrayList<>(List.of("java", "-jar", jar.toString()));
        commandLine.addAll(asList(args));
        return new ProcessBuilder(commandLine).directory(workingDir.toFile()).redirectErrorStream(false).start();
    }
}
