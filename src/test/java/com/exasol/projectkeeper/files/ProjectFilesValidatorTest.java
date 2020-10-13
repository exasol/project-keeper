package com.exasol.projectkeeper.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.Module;

class ProjectFilesValidatorTest {
    private static final List<Module> MODULES = List.of(Module.DEFAULT);

    @Test
    void test(@TempDir final File tempDir) throws IOException {
        final Log log = mock(Log.class);
        if (!tempDir.toPath().resolve(".settings/").toFile().mkdir()
                || !tempDir.toPath().resolve(".settings/org.eclipse.jdt.ui.prefs").toFile().createNewFile()) {
            throw new IllegalStateException("Failed to create test files.");
        }
        final ProjectFilesValidator validator = new ProjectFilesValidator(log);
        assertThat(validator.validateProjectStructure(tempDir, MODULES), equalTo(false));
        verify(log).error("Missing required: .settings/org.eclipse.jdt.core.prefs");
        verify(log).error("Missing required: README.md");
        verify(log).error("Outdated content: .settings/org.eclipse.jdt.ui.prefs");
    }

    @Test
    void testDifferentContent(@TempDir final File tempDir) throws MojoFailureException, IOException {
        final SystemStreamLog log = spy(new SystemStreamLog());
        new ProjectFilesFixer(log).fixProjectStructure(tempDir, MODULES);
        final File testFile = tempDir.toPath().resolve(".settings/org.eclipse.jdt.core.prefs").toFile();
        changeFile(testFile);
        MatcherAssert.assertThat(new ProjectFilesValidator(log).validateProjectStructure(tempDir, MODULES),
                equalTo(false));
        verify(log).error("Outdated content: .settings/org.eclipse.jdt.core.prefs");
    }

    private void changeFile(final File testFile) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write("something");
            fileWriter.flush();
        }
    }
}