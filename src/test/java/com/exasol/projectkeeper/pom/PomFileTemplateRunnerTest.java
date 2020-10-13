package com.exasol.projectkeeper.pom;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Module;

class PomFileTemplateRunnerTest {
    private static final String POM_WITH_NO_PLUGINS = "pomWithNoPlugins.xml";

    @Test
    void testMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = witeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileTemplateRunner runner = new PomFileTemplateRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        assertThat(runner.verify(log, Arrays.asList(Module.values())), equalTo(false));
        verify(log).error("Missing maven plugin org.codehaus.mojo:versions-maven-plugin.");
    }

    @Test
    void testFixMissingPlugin(@TempDir final File tempDir) throws IOException {
        final File pomFile = witeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileTemplateRunner runner = new PomFileTemplateRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        runner.fix(log, Arrays.asList(Module.values()));
        assertThat(runner.verify(log, Arrays.asList(Module.values())), equalTo(true));
    }

    @Test
    void testNoErrorsOnNoModules(@TempDir final File tempDir) throws IOException {
        final File pomFile = witeResourceToTempFile(tempDir, POM_WITH_NO_PLUGINS);
        final PomFileTemplateRunner runner = new PomFileTemplateRunner(pomFile);
        final Log log = spy(new SystemStreamLog());
        assertThat(runner.verify(log, List.of()), equalTo(true));
    }

    @NotNull
    private File witeResourceToTempFile(@TempDir final File tempDir, final String resourceName) throws IOException {
        final File pomFile = File.createTempFile("pom", ".xml", tempDir);
        try (final InputStream pomStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(Objects.requireNonNull(pomStream), pomFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return pomFile;
    }
}