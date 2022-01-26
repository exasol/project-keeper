package com.exasol.projectkeeper.validators.files;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.ProjectKeeperModule;

import lombok.Data;

/**
 * This class represents a template for project files.
 */
@Data
class FileTemplate {
    private final String templateInResources;
    private final Path pathInProject;
    /**
     * If true content must match. Otherwise, the file must only exist.
     */
    private final ProjectKeeperModule module;
    private final TemplateType templateType;

    /**
     * Create an instance of {@link FileTemplate} for a template from resources.
     * 
     * @param pathInProject path of the file in the project
     * @param module        project-keeper module
     * @param templateType  type of the template
     * @return built {@link FileTemplate}
     */
    static FileTemplate of(final String pathInProject, final ProjectKeeperModule module,
            final TemplateType templateType) {
        return new FileTemplate("templates/" + pathInProject, Path.of(pathInProject), module, templateType);
    }

    /**
     * Get the content of the template.
     * <p>
     * This method gets the content of the template dependant of the format of the current OS (windows / linux line
     * endings).
     * </p>
     *
     * @return template's content
     */
    public String getContent() {
        final String templateContent = readResource();
        return templateContent.replace("\r", "").replace("\n", System.lineSeparator());
    }

    private String readResource() {
        try (final InputStream resourceAsStream = getClass().getClassLoader()
                .getResourceAsStream(this.templateInResources)) {
            return new String(Objects.requireNonNull(resourceAsStream).readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-PK-CORE-57")
                    .message("Failed to read template {{template}}.", this.templateInResources).ticketMitigation()
                    .toString(), exception);
        }
    }

    /** Types of templates. */
    enum TemplateType {
        /** Verify the file has the same content as the template */
        REQUIRE_EXACT,
        /** Only check that the file exists. If not use the template's content as default. */
        REQUIRE_EXIST;
    }
}
