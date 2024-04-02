package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

/**
 * This {@link FileTemplate} allows to customize the content of a file template.
 */
class ContentCustomizingTemplate implements FileTemplate {

    private final FileTemplate delegate;
    private final ContentCustomizer customizer;

    ContentCustomizingTemplate(final FileTemplate delegate, final ContentCustomizer customizer) {
        this.delegate = delegate;
        this.customizer = customizer;
    }

    @Override
    public String getContent() {
        return customizer.customizeContent(delegate.getContent());
    }

    @Override
    public Path getPathInProject() {
        return delegate.getPathInProject();
    }

    @Override
    public Validation getValidation() {
        return delegate.getValidation();
    }

    /**
     * This interface allows to customize the content of a file template.
     */
    interface ContentCustomizer {
        String customizeContent(String content);
    }
}
