package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

import lombok.EqualsAndHashCode;

/**
 * {@link FileTemplate} that reads the content from a resource.
 */
@EqualsAndHashCode
public class FileTemplateFromResource implements FileTemplate {

    private final String templateResource;
    private final String pathInProject;
    private final Validation validation;

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param pathInProject path of the file in the project
     * @param validation    validation criteria for the template
     */
    public FileTemplateFromResource(final String pathInProject, final Validation validation) {
        this.templateResource = "templates/" + pathInProject;
        this.pathInProject = pathInProject;
        this.validation = validation;
    }

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param templateResource path to the template
     * @param pathInProject    path of the file in the project
     * @param validation       validation criteria for the template
     */
    public FileTemplateFromResource(final String templateResource, final String pathInProject,
            final Validation validation) {
        this.templateResource = templateResource;
        this.pathInProject = pathInProject;
        this.validation = validation;
    }

    @Override
    public Path getPathInProject() {
        return Path.of(this.pathInProject);
    }

    @Override
    public String getContent() {
        return new ResourceReader().readFromResource(this.templateResource);
    }

    @Override
    public Validation getValidation() {
        return this.validation;
    }
}
