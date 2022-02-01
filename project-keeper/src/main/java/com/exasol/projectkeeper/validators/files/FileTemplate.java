package com.exasol.projectkeeper.validators.files;

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
    java.nio.file.Path getPathInProject();

    /**
     * Get the template type
     * 
     * @return template type
     */
    TemplateType getTemplateType();

    /**
     * Types of templates.
     */
    public enum TemplateType {
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
