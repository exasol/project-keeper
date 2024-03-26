package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

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

    interface ContentCustomizer {
        String customizeContent(String content);
    }
}
