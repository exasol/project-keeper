package com.exasol.projectkeeper.cli;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;
import com.exasol.projectkeeper.sources.analyze.generic.SimpleProcess;
import com.exasol.projectkeeper.test.GolangProjectFixture;
import com.exasol.projectkeeper.test.MavenProjectFixture;

class ProjectKeeperLauncherExecutableJarIT {
    private static final Logger LOGGER = Logger.getLogger(ProjectKeeperLauncherExecutableJarIT.class.getName());
    private static final String PROJECT_ROOT_OFFSET = "../";
    private static final File PARENT_POM = Path.of(PROJECT_ROOT_OFFSET, "parent-pom/pom.xml").toFile();
    private static final String CURRENT_VERSION = MavenProjectVersionGetter.getProjectRevision(PARENT_POM.toPath());

    @TempDir
    Path projectDir;

    @Test
    void fixingMavenProjectSucceeds() {
        prepareMavenProject();
        assertProcessSucceeds(run(this.projectDir, "fix"), equalTo(""),
                containsString("[INFO   ] Created 'LICENSE'. Don't forget to update its content!"));
        assertProcessSucceeds(run(this.projectDir, "verify"), equalTo(""), containsString("Executing command"));
    }

    @Test
    void fixingGolangProjectSucceeds() {
        prepareGolangProject();
        assertProcessSucceeds(run(this.projectDir, "fix"), equalTo(""),
                containsString("[INFO   ] Created 'LICENSE'. Don't forget to update its content!"));
        assertProcessSucceeds(run(this.projectDir, "verify"), equalTo(""), containsString("Executing command"));
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
        @SuppressWarnings("resource")
        final GolangProjectFixture fixture = new GolangProjectFixture(this.projectDir);
        fixture.gitInit();
        fixture.prepareProjectFiles(fixture.createDefaultConfig());
    }

    private SimpleProcess run(final Path workingDir, final String... args) {
        final String artifactPrefix = "project-keeper-cli";// we need to split this in two lines so that it's not
                                                           // replaced by the artifact-reference-checker
        final Path jar = Path.of("target/" + artifactPrefix + "-" + CURRENT_VERSION + ".jar").toAbsolutePath();
        if (!Files.exists(jar)) {
            fail("Jar " + jar + " not found. Run 'mvn package' to build it.");
        }
        final List<String> commandLine = new ArrayList<>(List.of("java", "-jar", jar.toString()));
        commandLine.addAll(asList(args));
        LOGGER.info("Launching command " + commandLine + " in working dir '" + workingDir + "'...");
        return SimpleProcess.start(workingDir, commandLine);
    }

    private void assertProcessSucceeds(final SimpleProcess process, final Matcher<String> expectedOutput,
            final Matcher<String> expectedError) {
        process.waitUntilFinished(Duration.ofMinutes(2));
        assertAll(() -> assertThat("std output", process.getOutputStreamContent(), expectedOutput),
                () -> assertThat("std error", process.getErrorStreamContent(), expectedError));
    }
}
