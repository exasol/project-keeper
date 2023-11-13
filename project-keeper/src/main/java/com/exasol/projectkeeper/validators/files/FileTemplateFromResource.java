package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;

import com.exasol.errorreporting.ExaError;

/**
 * {@link FileTemplate} that reads the content from a resource.
 */
public final class FileTemplateFromResource implements FileTemplate {

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
     * Add a replacement definition. {@link FileTemplateFromResource} identifies variables in the template by prefix
     * {@code $} and replaces each variable with the value provided beforehand.
     *
     * @param name        name of the variable
     * @param replacement text that should be used to replace the variable
     * @return this for fluent programming
     */
    public FileTemplateFromResource replacing(final String name, final String replacement) {
        if (this.replacements.containsKey(name)) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-170").message(
                    "Cannot add replacement with value {{new replacement}} because {{name}} is already registered as replacement with value {{existing value}}.",
                    replacement, name, replacements.get(name)).mitigation("Remove or rename one of the replacements.")
                    .toString());
        }
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
            value = replacePlaceholder(value, rep);
        }
        return value;
    }

    private String replacePlaceholder(final String value, final Entry<String, String> rep) {
        final String newValue = value.replace("$" + rep.getKey(), rep.getValue());
        if (value.equals(newValue)) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-169")
                    .message("Placeholder {{placeholder}} not found in template {{template}}.", rep.getKey(),
                            this.templateResource)
                    .ticketMitigation().toString());
        }
        return newValue;
    }

    @Override
    public Validation getValidation() {
        return this.validation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateResource, pathInProject, validation, replacements);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FileTemplateFromResource other = (FileTemplateFromResource) obj;
        return Objects.equals(templateResource, other.templateResource)
                && Objects.equals(pathInProject, other.pathInProject) && validation == other.validation
                && Objects.equals(replacements, other.replacements);
    }
}
