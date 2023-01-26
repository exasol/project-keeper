package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.EqualsAndHashCode;

/**
 * {@link FileTemplate} that reads the content from a resource.
 */
@EqualsAndHashCode
public class FileTemplateFromResource implements FileTemplate {

    private final String templateResource;
    private final String pathInProject;
    private final Validation validation;
    private final Map<String, String> replacements = new HashMap<>();

    /**
     * Create a new instance of {@link FileTemplateFromResource}.
     *
     * @param pathInProject path of the file in the project
     * @param validation    validation criteria for the template
     */
    public FileTemplateFromResource(final String pathInProject, final Validation validation) {
        this("templates/" + pathInProject, pathInProject, validation);
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

    /**
     * Add a replacement definition. {@link FileTemplateFromResource} identifies variables in the template by prefix "$"
     * and replaces each variable with the value provided beforehand.
     *
     * @param name        name of the variable
     * @param replacement text that should be used to replace the variable
     * @return this for fluent programming
     */
    public FileTemplateFromResource replacing(final String name, final String replacement) {
        this.replacements.put(name, replacement);
        return this;
    }

    @Override
    public Path getPathInProject() {
        return Path.of(this.pathInProject);
    }

    @Override
    public String getContent() {
        String value = new ResourceReader().readFromResource(this.templateResource);
        for (final Entry<String, String> rep : this.replacements.entrySet()) {
            value = value.replace("$" + rep.getKey(), rep.getValue());
        }
        return value;
    }

    @Override
    public Validation getValidation() {
        return this.validation;
    }
}
