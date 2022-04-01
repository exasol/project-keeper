package com.exasol.projectkeeper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LicenseNameReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadLicenseName() throws IOException {
        Files.writeString(this.tempDir.resolve("LICENSE"), "My License\n\n© A developer\n\nThe license text.");
        final String licenseName = new LicenseNameReader().readLicenseName(this.tempDir);
        assertThat(licenseName, equalTo("My License"));
    }

    @Test
    void testLicenseFileMissing() {
        final LicenseNameReader reader = new LicenseNameReader();
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> reader.readLicenseName(this.tempDir));
        assertThat(exception.getMessage(), equalTo(
                "F-PK-CORE-139: Failed to read LICENSE file. That's strange because it should have been created by the LicenseValidator. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }
}