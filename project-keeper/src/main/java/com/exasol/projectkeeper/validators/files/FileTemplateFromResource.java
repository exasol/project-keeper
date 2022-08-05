package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * {@link FileTemplate} that reads the content from a resource.
 */
@EqualsAndHashCode
public class FileTemplateFromResource implements FileTemplate {

    private final String templateResource;
    private final String pathInProject;
    @Getter
    private final TemplateType templateType;

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param pathInProject path of the file in the project
     * @param templateType  type of the template
     */
    public FileTemplateFromResource(final String pathInProject, final TemplateType templateType) {
        this.templateResource = "templates/" + pathInProject;
        this.pathInProject = pathInProject;
        this.templateType = templateType;
    }

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param templateResource path to the template
     * @param pathInProject    path of the file in the project
     * @param templateType     type of the template
     */
    public FileTemplateFromResource(final String templateResource, final String pathInProject,
            final TemplateType templateType) {
        this.templateResource = templateResource;
        this.pathInProject = pathInProject;
        this.templateType = templateType;
    }

    @Override
    public Path getPathInProject() {
        return Path.of(this.pathInProject);
    }

    @Override
    public String getContent() {
        return new ResourceReader().readFromResource(this.templateResource);
    }
}
