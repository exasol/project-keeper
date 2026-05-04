package com.exasol.projectkeeper.validators.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.exasol.errorreporting.ExaError;

class ResourceReader {
    String readFromResource(final String resourceName) {
        final String templateContent = readResource(resourceName);
        return templateContent.replace("\r", "").replace("\n", System.lineSeparator());
    }

    private String readResource(final String resourceName) {
        try (final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (resourceAsStream == null) {
                throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-213")
                        .message("Template not found for resource name {{resource name}}.", resourceName)
                        .ticketMitigation().toString());
            }
            return new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException | NullPointerException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-57")
                    .message("Failed to read template from resource {{resource name}}.", resourceName)
                    .ticketMitigation().toString(), exception);
        }
    }
}
