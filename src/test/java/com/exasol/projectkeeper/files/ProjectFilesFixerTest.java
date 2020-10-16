package com.exasol.projectkeeper.files;

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

import com.exasol.projectkeeper.ProjectKeeperModule;

class ProjectFilesFixerTest {
    private static final List<ProjectKeeperModule> PROJECT_KEEPER_MODULES = List.of(ProjectKeeperModule.DEFAULT);

    @Test
    void testFixing(@TempDir final File tempDir) throws MojoFailureException {
        final ProjectFilesFixer fixer = new ProjectFilesFixer(new SystemStreamLog());
        fixer.fixProjectStructure(tempDir, PROJECT_KEEPER_MODULES);
        assertThat(tempDir.toPath().resolve("README.md").toFile(), anExistingFile());
    }

    @Test
    void validateAfterFix(@TempDir final File tempDir) throws MojoFailureException {
        final SystemStreamLog log = new SystemStreamLog();
        new ProjectFilesFixer(log).fixProjectStructure(tempDir, PROJECT_KEEPER_MODULES);
        assertThat(new ProjectFilesValidator(log).validateProjectStructure(tempDir, PROJECT_KEEPER_MODULES),
                equalTo(true));
    }

    @Test
    void testFailedToCreate(@TempDir final File tempDir) {
        if (!tempDir.setWritable(false)) {
            throw new IllegalStateException("Failed to set temp dir read-only.");
        }
        final SystemStreamLog log = spy(new SystemStreamLog());
        final ProjectFilesFixer fixer = new ProjectFilesFixer(log);
        assertThrows(MojoFailureException.class, () -> fixer.fixProjectStructure(tempDir, PROJECT_KEEPER_MODULES));
        verify(log).error(and(startsWith("E-PK-16 Failed to create or replace"), endsWith("README.md")));
    }
}