package com.exasol.projectkeeper.pom;

import static com.exasol.projectkeeper.pom.PomTesting.POM_WITH_NO_PLUGINS;
import static com.exasol.projectkeeper.pom.PomTesting.writeResourceToTempFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ProjectKeeperModule;

class PomFileValidationRunnerTest {

    @Test
    void testMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidationRunner runner = new PomFileValidationRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        assertThat(runner.verify(log, Arrays.asList(ProjectKeeperModule.values())), equalTo(false));
        verify(log).error("E-PK-15 Missing maven plugin org.codehaus.mojo:versions-maven-plugin.");
    }

    @Test
    void testFixMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidationRunner runner = new PomFileValidationRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        runner.fix(log, Arrays.asList(ProjectKeeperModule.values()));
        assertThat(runner.verify(log, Arrays.asList(ProjectKeeperModule.values())), equalTo(true));
    }

    @Test
    void testNoErrorsOnNoModules(@TempDir final File tempDir) throws IOException {
        final File pomFile = writeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileValidationRunner runner = new PomFileValidationRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        assertThat(runner.verify(log, List.of()), equalTo(true));
    }
}