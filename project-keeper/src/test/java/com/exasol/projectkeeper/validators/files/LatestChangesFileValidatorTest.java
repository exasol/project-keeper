package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.startsWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.validators.changesfile.ChangesFileName;
import com.exasol.projectkeeper.validators.finding.SimpleValidationFinding;

class LatestChangesFileValidatorTest {

    @Test
    void emptyFolder(@TempDir final Path tempDir) throws IOException {
        assertThat(testee(tempDir).validate(), empty());
    }

    @Test
    void noFinding(@TempDir final Path tempDir) throws IOException {
        final LatestChangesFileValidator testee = testee(tempDir, "1.0.1", "2.0.0");
        assertThat(testee.validate(), empty());
    }

    @Test
    void finding(@TempDir final Path tempDir) throws IOException {
        final LatestChangesFileValidator testee = testee(tempDir, "1.0.1", "3.0.0");
        final SimpleValidationFinding finding = (SimpleValidationFinding) testee.validate().get(0);
        assertThat(finding.getMessage(), startsWith("E-PK-CORE-162: Found newer changes file 'changes_3.0.0.md'."));
    }

    private LatestChangesFileValidator testee(final Path tempDir, final String... versions) throws IOException {
        final Path folder = tempDir.resolve(Path.of("doc", "changes"));
        Files.createDirectories(folder);
        for (final String v : versions) {
            final ChangesFileName cfile = new ChangesFileName(v);
            Files.createFile(folder.resolve(cfile.filename()));
        }
        return new LatestChangesFileValidator(tempDir, "2.0.0");
    }
}
