package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXIST;

import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.*;

/**
 * Factory for file templates.
 */
class FileTemplatesFactory {

    private static final String POM_FILES_GENERATED = String.format("%-65s%s", "pk_generated_parent.pom",
            "linguist-generated=true");

    private static final String NPM_SETUP = lines( //
            "      - name: Set up NPM", //
            "        uses: actions/setup-node@v3", //
            "        with:", //
            "          node-version: 16", //
            "          cache: \"npm\"", //
            "          cache-dependency-path: \"**/package-lock.json\"" //
    );

    private static String lines(final String... line) {
        return String.join("\n", Arrays.asList(line));
    }

    private final Logger logger;
    private final String ownVersion;
    private final boolean hasNpmModule;

    public FileTemplatesFactory(final Logger logger, final String ownVersion, final boolean hasNpmModule) {
        this.logger = logger;
        this.ownVersion = ownVersion;
        this.hasNpmModule = hasNpmModule;
    }

    List<FileTemplate> getGlobalTemplates(final List<AnalyzedSource> sources) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(new FileTemplateFromResource(".github/workflows/broken_links_checker.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource("release_config.yml", REQUIRE_EXIST));
        templates.add(new FileTemplateFromResource(".vscode/settings.json", REQUIRE_EXIST));
        final Optional<AnalyzedSource> mvnRoot = sources.stream().filter(this::isMvnRootProject).findFirst();
        templates.add(new FileTemplateFromResource("templates/gitattributes", ".gitattributes", REQUIRE_EXIST)
                .replacing("pomFiles", mvnRoot.isPresent() ? POM_FILES_GENERATED : ""));
        if (mvnRoot.isPresent()) {
            templates.addAll(getGenericMavenTemplates(mvnRoot.get().getModules()));
            if (mvnRoot.get().getModules().contains(MAVEN_CENTRAL)) {
                templates.add(new FileTemplateFromResource(
                        ".github/workflows/release_droid_release_on_maven_central.yml", REQUIRE_EXACT));
            }
        } else {
            templates.addAll(getProjectKeeperVerifyWorkflowTemplates());
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-91")
                    .message("For this project structure project keeper does not know how to configure ci-build.")
                    .mitigation("Please create the required actions on your own.").toString());
        }
        return templates;
    }

    private List<FileTemplate> getGenericMavenTemplates(final Set<ProjectKeeperModule> modules) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(getCiBuildTemplate(modules));
        templates.add(new FileTemplateFromResource(".github/workflows/ci-build-next-java.yml", REQUIRE_EXACT) //
                .replacing("skipNativeImage", //
                        modules.contains(ProjectKeeperModule.NATIVE_IMAGE) ? "-P skipNativeImage" : ""));
        templates.add(new FileTemplateFromResource(".github/workflows/dependencies_check.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_prepare_original_checksum.yml",
                REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_print_quick_checksum.yml",
                REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".github/workflows/release_droid_upload_github_release_assets.yml",
                REQUIRE_EXACT));
        return templates;
    }

    private FileTemplateFromResource getCiBuildTemplate(final Set<ProjectKeeperModule> modules) {
        if (modules.contains(NATIVE_IMAGE)) {
            return new FileTemplateFromResource("templates/.github/workflows/ci-build-native-build.yml",
                    ".github/workflows/ci-build.yml", REQUIRE_EXACT);
        } else {
            return new FileTemplateFromResource(".github/workflows/ci-build.yml", REQUIRE_EXACT);
        }
    }

    private boolean isMvnRootProject(final AnalyzedSource source) {
        return (source instanceof AnalyzedMavenSource) && (((AnalyzedMavenSource) source).isRootProject());
    }

    List<FileTemplate> getTemplatesForSource(final AnalyzedSource source) {
        if (source instanceof AnalyzedMavenSource) {
            return getMavenTemplates((AnalyzedMavenSource) source);
        } else if (source instanceof AnalyzedSourceImpl) {
            // do not verify templates (e.g. .github/workflows) for submodules
            return Collections.emptyList();
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
        if (enabledModules.contains(NATIVE_IMAGE)) {
            templates.add(new FileTemplateFromResource("src/main/reflect-config.json", REQUIRE_EXIST));
        }
        return templates;
    }

    private List<FileTemplate> getProjectKeeperVerifyWorkflowTemplates() {
        final ArrayList<FileTemplate> templates = new ArrayList<>();
        final String pathInProject = ".github/workflows/project-keeper-verify.yml";
        templates.add(new FileTemplateFromResource("non_maven_templates/" + pathInProject, //
                pathInProject, REQUIRE_EXACT) //
                        .replacing("npmSetup", this.hasNpmModule ? NPM_SETUP : ""));
        templates.add(new ProjectKeeperShellScript(this.ownVersion));
        return templates;
    }
}
