package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.FileTemplate.TemplateType.REQUIRE_EXIST;
import static java.util.Collections.emptyList;

import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.*;

import lombok.RequiredArgsConstructor;

/**
 * Factory for file templates.
 */
@RequiredArgsConstructor
class FileTemplatesFactory {
    private final Logger logger;

    List<FileTemplate> getGlobalTemplates(final List<AnalyzedSource> sources) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(new FileTemplateFromResource(".github/workflows/broken_links_checker.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource("release_config.yml", REQUIRE_EXIST));
        final Optional<AnalyzedSource> mvnRoot = sources.stream().filter(this::isMvnRootProject).findFirst();
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
        templates.add(new FileTemplateFromResource(".github/workflows/ci-build.yml", REQUIRE_EXACT));
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

    private boolean isMvnRootProject(final AnalyzedSource source) {
        return (source instanceof AnalyzedMavenSource) && (((AnalyzedMavenSource) source).isRootProject());
    }

    List<FileTemplate> getTemplatesForSource(final AnalyzedSource source) {
        if (source instanceof AnalyzedMavenSource) {
            return getMavenTemplates((AnalyzedMavenSource) source);
        } else if (source instanceof AnalyzedGolangSource) {
            return getGolangTemplates();
        } else {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-137")
                    .message("Cannot get templates for unknown source type {{type}}", source.getClass().getSimpleName())
                    .toString());
        }
    }

    private List<FileTemplate> getMavenTemplates(final AnalyzedMavenSource source) {
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

    private List<FileTemplate> getGolangTemplates() {
        return emptyList();
    }
}
