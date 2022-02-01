package com.exasol.projectkeeper.validators.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.exasol.errorreporting.ExaError;

class ResourceReader {
    String readFromResource(final String resourceName) {
        final String templateContent = readResource(resourceName);
        return templateContent.replace("\r", "").replace("\n", System.lineSeparator());
    }

    private String readResource(final String resourceName) {
        try (final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            return new String(Objects.requireNonNull(resourceAsStream).readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException | NullPointerException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-57")
                    .message("Failed to read template from resource {{resource name}}.", resourceName)
                    .ticketMitigation().toString(), exception);
        }
    }
}
