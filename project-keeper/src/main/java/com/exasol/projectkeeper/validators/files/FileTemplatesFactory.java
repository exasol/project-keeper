package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXIST;

import java.nio.file.Path;
import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.config.ProjectKeeperConfig;

import lombok.RequiredArgsConstructor;

/**
 * Factory for file templates.
 */
@RequiredArgsConstructor
class FileTemplatesFactory {
    private final Logger logger;

    List<FileTemplate> getGlobalTemplates(final Path projectDir, final List<ProjectKeeperConfig.Source> sources) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(new FileTemplateFromResource(".github/workflows/broken_links_checker.yml", REQUIRE_EXACT));
        final Optional<ProjectKeeperConfig.Source> mvnRoot = sources.stream()
                .filter(source -> isMvnRootProject(projectDir, source)).findFirst();
        if (mvnRoot.isPresent()) {
            templates.addAll(getGenericMavenTemplates());
            if (mvnRoot.get().getModules().contains(MAVEN_CENTRAL)) {
                templates.add(new FileTemplateFromResource(
                        ".github/workflows/release_droid_release_on_maven_central.yml", REQUIRE_EXACT));
            }
        } else {
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-91")
                    .message("For this project structure project keeper does not know how to configure ci-build.")
                    .mitigation("Please create the required actions on your own.").toString());
        }
        return templates;
    }

    private List<FileTemplate> getGenericMavenTemplates() {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(new FileTemplateFromResource(".github/workflows/ci-build.yml", REQUIRE_EXIST));
        templates.add(new FileTemplateFromResource(".github/workflows/ci-build-next-java.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/dependencies_check.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_prepare_original_checksum.yml",
                REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_print_quick_checksum.yml",
                REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_upload_github_release_assets.yml",
                REQUIRE_EXACT));
        return templates;
    }

    private boolean isMvnRootProject(final Path projectDir, final ProjectKeeperConfig.Source source) {
        final Path relativePath = projectDir.relativize(source.getPath());
        return relativePath.equals(Path.of("pom.xml")) && source.getType().equals(MAVEN);
    }

    List<FileTemplate> getTemplatesForSource(final ProjectKeeperConfig.Source source) {
        final List<FileTemplate> templates = new ArrayList<>();
        final Set<ProjectKeeperModule> enabledModules = source.getModules();
        if (enabledModules.contains(DEFAULT)) {
            templates.add(new FileTemplateFromResource(".settings/org.eclipse.jdt.ui.prefs", REQUIRE_EXACT));
            templates.add(new FileTemplateFromResource(".settings/org.eclipse.jdt.core.prefs", REQUIRE_EXACT));
            templates.add(new FileTemplateFromResource("src/test/resources/logging.properties", REQUIRE_EXACT));
            templates.add(new FileTemplateFromResource("versionsMavenPluginRules.xml", REQUIRE_EXACT));
        }
        if (enabledModules.contains(JAR_ARTIFACT)) {
            templates.add(new FileTemplateFromResource("src/assembly/all-dependencies.xml", REQUIRE_EXACT));
        }
        if (enabledModules.contains(LOMBOK)) {
            templates.add(new FileTemplateFromResource("lombok.config", REQUIRE_EXACT));
        }
        return templates;
    }
}
