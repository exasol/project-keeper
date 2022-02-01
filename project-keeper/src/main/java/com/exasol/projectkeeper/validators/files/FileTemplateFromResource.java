package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

import lombok.*;

/**
 * {@link FileTemplate} that reads the content from a resources.
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class FileTemplateFromResource implements FileTemplate {
    private final String pathInProject;
    @Getter
    private final TemplateType templateType;

    @Override
    public Path getPathInProject() {
        return Path.of(this.pathInProject);
    }

    @Override
    public String getContent() {
        return new ResourceReader().readFromResource("templates/" + this.pathInProject);
    }
}
