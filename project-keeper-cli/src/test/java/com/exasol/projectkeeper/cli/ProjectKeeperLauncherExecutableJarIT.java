package com.exasol.projectkeeper.cli;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;
import com.exasol.projectkeeper.sources.analyze.golang.SimpleProcess;
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
    void fixingMavenProjectSucceeds() throws InterruptedException, IOException {
        prepareMavenProject();
        assertProcessSucceeds(run(this.projectDir, "fix"));
        assertProcessSucceeds(run(this.projectDir, "verify"));
    }

    @Test
    void fixingGolangProjectSucceeds() throws InterruptedException, IOException {
        prepareGolangProject();
        assertProcessSucceeds(run(this.projectDir, "fix"));
        assertProcessSucceeds(run(this.projectDir, "verify"));
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

    private SimpleProcess run(final Path workingDir, final String... args) throws IOException {
        final String artifactPrefix = "project-keeper-cli";// we need to split this in two lines so that it's not
                                                           // replaced by the artifact-reference-checker
        final Path jar = Paths.get("target/" + artifactPrefix + "-" + CURRENT_VERSION + ".jar").toAbsolutePath();
        if (!Files.exists(jar)) {
            fail("Jar " + jar + " not found. Run 'mvn package' to build it.");
        }
        final List<String> commandLine = new ArrayList<>(List.of("java", "-jar", jar.toString()));
        commandLine.addAll(asList(args));
        LOGGER.info("Launching command " + commandLine + " in working dir '" + workingDir + "'...");
        return SimpleProcess.start(workingDir, commandLine);
    }

    private void assertProcessSucceeds(final SimpleProcess process) {
        process.getOutput(Duration.ofSeconds(60));
    }
}
