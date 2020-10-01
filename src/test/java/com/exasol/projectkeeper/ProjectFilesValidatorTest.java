package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProjectFilesValidatorTest {

    @Test
    void test(@TempDir final File tempDir) throws IOException {
        final Log log = mock(Log.class);
        if (!tempDir.toPath().resolve(".settings/").toFile().mkdir()
                || !tempDir.toPath().resolve(".settings/org.eclipse.jdt.ui.prefs").toFile().createNewFile()) {
            throw new IllegalStateException("Failed to create test files.");
        }
        final ProjectFilesValidator validator = new ProjectFilesValidator(log);
        assertThat(validator.validateProjectStructure(tempDir, List.of("default")), equalTo(false));
        verify(log).error("Missing required: .settings/org.eclipse.jdt.core.prefs");
        verify(log).error("Missing required: README.md");
        verify(log).error("Outdated content: .settings/org.eclipse.jdt.ui.prefs");
    }
}