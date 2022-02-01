package com.exasol.projectkeeper.validators.files;

public interface FileTemplate {
    String getContent();

    java.nio.file.Path getPathInProject();

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
