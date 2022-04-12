package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

import lombok.Getter;

/**
 * {@link FileTemplate} with empty content.
 */
class EmptyFileTemplate implements FileTemplate {
    @Getter
    private final Path pathInProject;
    @Getter
    private final TemplateType templateType;

    public EmptyFileTemplate(final String pathInProject, final TemplateType templateType) {
        this.pathInProject = Path.of(pathInProject);
        this.templateType = templateType;
    }

    @Override
    public String getContent() {
        return "";
    }
}
