package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * {@link FileTemplate} that reads the content from a resource.
 */
@EqualsAndHashCode
public class FileTemplateFromResource implements FileTemplate {
    private final String pathInProject;
    private final String pathToTemplate;
    @Getter
    private final TemplateType templateType;

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     * 
     * @param pathInProject path of the file in the project
     * @param templateType  type of the template
     */
    public FileTemplateFromResource(final String pathInProject, final TemplateType templateType) {
        this.pathInProject = pathInProject;
        this.pathToTemplate = "templates/" + pathInProject;
        this.templateType = templateType;
    }

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param pathInProject  path of the file in the project
     * @param pathToTemplate path to the template
     * @param templateType   type of the template
     */
    public FileTemplateFromResource(final String pathInProject, final String pathToTemplate,
            final TemplateType templateType) {
        this.pathInProject = pathInProject;
        this.pathToTemplate = pathToTemplate;
        this.templateType = templateType;
    }

    @Override
    public Path getPathInProject() {
        return Path.of(this.pathInProject);
    }

    @Override
    public String getContent() {
        return new ResourceReader().readFromResource(this.pathToTemplate);
    }
}
