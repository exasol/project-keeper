package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

/** Interface for templates for project files. */
public interface FileTemplate {
    /**
     * Get the expected content.
     *
     * @return expected content
     */
    String getContent();

    /**
     * Get the path of the file in the project.
     *
     * @return path
     */
    Path getPathInProject();

    /**
     * Get the template type
     *
     * @return template type
     */
    Validation getValidation();

    /**
     * Check that the file is executable on platforms that support executable file permissions.
     *
     * @return {@code true} if the template represents an executable script
     */
    default boolean requiresExecutablePermission() {
        return false;
    }

    /**
     * Types of templates.
     */
    public enum Validation {
        /**
         * Verify the file has the same content as the template
         */
        REQUIRE_EXACT,
        /**
         * Only check that the file exists. If not use the template's content as default.
         */
        REQUIRE_EXIST;
    }
}
