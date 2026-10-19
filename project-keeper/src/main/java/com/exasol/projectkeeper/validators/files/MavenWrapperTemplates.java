package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;

import java.util.List;

/**
 * Creates templates for the Maven Wrapper files.
 */
final class MavenWrapperTemplates {
    private static final List<String> PATHS = List.of("mvnw", "mvnw.cmd", ".mvn/wrapper/maven-wrapper.properties");

    private MavenWrapperTemplates() {
        // utility class
    }

    static List<FileTemplate> getTemplates() {
        return PATHS.stream()
                .map(MavenWrapperTemplates::createTemplate)
                .toList();
    }

    private static FileTemplate createTemplate(final String path) {
        final FileTemplateFromResource template = new FileTemplateFromResource("templates/" + path, path, REQUIRE_EXACT);
        if ("mvnw".equals(path)) {
            template.executable();
        }
        return template;
    }
}
