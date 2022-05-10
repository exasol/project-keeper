package com.exasol.projectkeeper.cli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
        assertProcessFails(args, "E-PK-CLI-2: Got no or invalid command line argument '" + Arrays.toString(args)
                + "'. Please only specify arguments 'verify' or 'fix'.");
    }

    @Test
    void runMainMethodWithNullArgumentFails() throws IOException, InterruptedException {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> ProjectKeeperLauncher.main(null));
        assertThat(exception.getMessage(), equalTo("E-PK-CLI-2: Got no or invalid command line argument 'null'. Please only specify arguments 'verify' or 'fix'."));
    }

    @Test
    void verifyFailsForProjectWithoutGit() throws IOException, InterruptedException {
        assertProcessFails("verify", "E-PK-CORE-90: Could not find .git directory in project-root " + projectDir
                + ". Known mitigations:\n* Run 'git init'.\n* Make sure that you run project-keeper only in the root directory of the git-repository. If you have multiple projects in that directory, define them in the '.project-keeper.yml'.");
    }

    @Test
    void cliDoesNotSupportMavenProjects() throws InterruptedException, IOException {
        prepareMavenProject();
        assertProcessFails("fix", "F-PK-CORE-145: Analyzing Maven projects in standalone mode is not supported. Use project-keeper-maven-plugin for analyzing Maven projects.");
    }

    @Test
    void fixingGolangProjectSucceeds() throws InterruptedException, IOException {
        prepareGolangProject();
        assertProcessSucceeds("fix");
        assertProcessSucceeds("verify");
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

    private void assertProcessSucceeds(final String command) throws InterruptedException {
        final ProjectKeeperLauncher launcher = new ProjectKeeperLauncher(this.projectDir);
        assertDoesNotThrow(() -> launcher.start(new String[] { command }));
    }

    private void assertProcessFails(final String argument, final String expectedErrorMessage)
            throws InterruptedException, IOException {
        assertProcessFails(new String[] { argument }, expectedErrorMessage);
    }

    private void assertProcessFails(final String[] arguments, final String expectedErrorMessage)
            throws InterruptedException, IOException {
        final ProjectKeeperLauncher launcher = new ProjectKeeperLauncher(this.projectDir);
        final Exception actualException = assertThrows(Exception.class, () -> launcher.start(arguments));
        assertThat(actualException.getMessage(), equalTo(expectedErrorMessage));
    }
}
