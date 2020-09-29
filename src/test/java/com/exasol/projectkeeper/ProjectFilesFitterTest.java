package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.AbstractProjectKeeperMojo.MODULE_DEFAULT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.and;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProjectFilesFitterTest {
    private static final List<String> MODULES = List.of(MODULE_DEFAULT);

    @Test
    void testFitting(@TempDir final File tempDir) throws MojoFailureException {
        final ProjectFilesFitter fitter = new ProjectFilesFitter(new SystemStreamLog());
        fitter.fitProjectStructure(tempDir, MODULES);
        assertThat(tempDir.toPath().resolve("README.md").toFile(), anExistingFile());
    }

    @Test
    void validateAfterFit(@TempDir final File tempDir) throws MojoFailureException {
        final SystemStreamLog log = new SystemStreamLog();
        new ProjectFilesFitter(log).fitProjectStructure(tempDir, MODULES);
        assertThat(new ProjectFilesValidator(log).validateProjectStructure(tempDir, MODULES), equalTo(true));
    }

    @Test
    void testFailedToCreate(@TempDir final File tempDir) {
        if (!tempDir.setWritable(false)) {
            throw new IllegalStateException("Failed to set temp dir read-only.");
        }
        final SystemStreamLog log = spy(new SystemStreamLog());
        final ProjectFilesFitter fitter = new ProjectFilesFitter(log);
        assertThrows(MojoFailureException.class, () -> fitter.fitProjectStructure(tempDir, MODULES));
        verify(log).error(and(startsWith("Failed to create or replace"), endsWith("README.md")));
    }
}