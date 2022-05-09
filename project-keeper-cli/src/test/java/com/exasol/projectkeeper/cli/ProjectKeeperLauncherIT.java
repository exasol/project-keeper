package com.exasol.projectkeeper.cli;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.projectkeeper.test.GolangProjectFixture;
import com.exasol.projectkeeper.test.MavenProjectFixture;

class ProjectKeeperLauncherIT {
    private static final Logger LOGGER = Logger.getLogger(ProjectKeeperLauncherIT.class.getName());

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
        assertProcessFails(process, 1, "E-PK-CLI-2: Got no or invalid command line argument");
    }

    @Test
    void verifyFailsForProjectWithoutGit() throws IOException, InterruptedException {
        final Process process = run(this.projectDir, "verify");
        assertProcessFails(process, 1, "E-PK-CORE-90: Could not find .git directory in project-root");
    }

    @Test
    void fixingFailsForProjectWithoutGit() throws IOException, InterruptedException {
        final Process process = run(this.projectDir, "fix");
        assertProcessFails(process, 1, "E-PK-CORE-90: Could not find .git directory in project-root");
    }

    @Test
    void cliDoesNotSupportMavenProjects() throws InterruptedException, IOException {
        prepareMavenProject();
        assertProcessFails(run(this.projectDir, "fix"), 1,
                "F-PK-CORE-145: Analyzing Maven projects in standalone mode is not supported");
    }

    @Test
    void fixingGolangProjectSucceeds() throws InterruptedException, IOException {
        prepareGolangProject();
        assertProcessSucceeds(run(this.projectDir, "fix"),
                "[WARNING] Created '.gitignore'. Don't forget to update it's content!");
        assertProcessSucceeds(run(this.projectDir, "verify"),
                "[WARNING] W-PK-CORE-91: For this project structure project keeper does not know how to configure ci-build. Please create the required actions on your own.");
    }

    private void prepareMavenProject() {
        LOGGER.info("Preparing Maven project in " + this.projectDir);
        final MavenProjectFixture fixture = new MavenProjectFixture(this.projectDir);
        fixture.gitInit();
        fixture.writeConfig(fixture.getConfigWithoutModulesBuilder());
        fixture.writeDefaultPom();
    }

    private void prepareGolangProject() {
        LOGGER.info("Preparing Golang project in " + this.projectDir);
        final GolangProjectFixture fixture = new GolangProjectFixture(this.projectDir);
        fixture.gitInit();
        fixture.prepareProjectFiles(fixture.createDefaultConfig());
    }

    private Process run(final Path workingDir, final String... args) throws IOException {
        final Path jar = Paths.get("target/project-keeper-cli-2.4.2.jar").toAbsolutePath();
        if (!Files.exists(jar)) {
            fail("Jar " + jar + " not found. Run 'mvn package' to build it.");
        }
        final List<String> commandLine = new ArrayList<>(List.of("java", "-jar", jar.toString()));
        commandLine.addAll(asList(args));
        LOGGER.info("Launching command " + commandLine + " in working dir '" + workingDir + "'...");
        return new ProcessBuilder(commandLine).directory(workingDir.toFile()).redirectErrorStream(false).start();
    }

    private void assertProcessSucceeds(final Process process, final String expectedMessage)
            throws InterruptedException {
        final int exitCode = process.waitFor();
        final String stdOut = readString(process.getInputStream());
        final String stdErr = readString(process.getErrorStream());
        if (exitCode != 0) {
            LOGGER.warning("Process failed with message\n---\n" + stdErr + "\n---");
        }
        assertAll(() -> assertThat(exitCode, equalTo(0)), //
                () -> assertThat("std error", stdErr, containsString(expectedMessage)), //
                () -> assertThat("std output", stdOut, equalTo("")));
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

    private String readString(final InputStream stream) {
        try {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }
}
