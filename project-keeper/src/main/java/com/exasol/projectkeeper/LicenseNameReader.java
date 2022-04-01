package com.exasol.projectkeeper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.exasol.errorreporting.ExaError;

public class LicenseNameReader {

    /**
     * Read the name of the license (first line of LICENSE file).
     * 
     * @param projectDir project directory
     * @return name of the license
     */
    public String readLicenseName(final Path projectDir) {
        final String licenseFile = readLicenseFile(projectDir);
        return getFirstLine(licenseFile);
    }

    private String getFirstLine(final String licenseFile) {
        return licenseFile.split("\n")[0];
    }

    private String readLicenseFile(final Path projectDir) {
        try {
            return Files.readString(projectDir.resolve("LICENSE"));
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-124").message(
                    "Failed to read LICENSE file. That's strange because it should have been created by the LicenseValidator.")
                    .ticketMitigation().toString(), exception);
        }
    }
}
