package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;

class ProjectKeeperShellScript implements FileTemplate {
    private static final String TEMPLATE_PATH = ".github/workflows/project-keeper.sh";

    private final String ownVersion;

    ProjectKeeperShellScript(final String ownVersion) {
        this.ownVersion = ownVersion;
    }

    @Override
    public String getContent() {
        String template = new ResourceReader().readFromResource("non_maven_templates/" + TEMPLATE_PATH);
        template = template.replace("##VERSION##", this.ownVersion);
        return template;
    }

    @Override
    public Path getPathInProject() {
        return Path.of(TEMPLATE_PATH);
    }

    @Override
    public Validation getValidation() {
        return Validation.REQUIRE_EXACT;
    }
}
