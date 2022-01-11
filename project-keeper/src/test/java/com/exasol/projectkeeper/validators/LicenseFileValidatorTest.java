package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Collections;

import org.apache.maven.plugin.logging.Log;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.projectkeeper.ExcludedFilesMatcher;

//[utest->dsn~license-file-validator~1]
class LicenseFileValidatorTest {
    @Test
    void testCreateFile(@TempDir final Path tempDir) throws IOException {
        getValidator(tempDir).validate().forEach(finding -> finding.getFix().fixError(mock(Log.class)));
        final String readme = Files.readString(tempDir.resolve("LICENSE"));
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        assertThat(readme, Matchers.containsString("Copyright (c) " + currentYear));
    }

    private LicenseFileValidator getValidator(final Path tempDir) {
        return new LicenseFileValidator(tempDir, new ExcludedFilesMatcher(Collections.emptyList()));
    }
}