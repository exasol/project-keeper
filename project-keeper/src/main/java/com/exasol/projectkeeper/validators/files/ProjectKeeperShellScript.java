package com.exasol.projectkeeper.validators.files;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        return Paths.get(TEMPLATE_PATH);
    }

    @Override
    public TemplateType getTemplateType() {
        return TemplateType.REQUIRE_EXACT;
    }
}
