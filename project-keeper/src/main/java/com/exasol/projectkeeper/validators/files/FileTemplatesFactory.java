package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.*;
import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXIST;

import java.util.*;

import com.exasol.errorreporting.ExaError;
import com.exasol.projectkeeper.Logger;
import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.sources.*;

/**
 * Factory for file templates.
 */
class FileTemplatesFactory {
    private static final String POM_FILES_GENERATED = String.format("%-65s%s", "pk_generated_parent.pom",
            "linguist-generated=true");

    private final Logger logger;
    private final String ownVersion;
    private final boolean hasNpmModule;
    private final BuildOptions buildOptions;
    private final CiBuildWorkflowGenerator workflowGenerator;

    public FileTemplatesFactory(final Logger logger, final String ownVersion, final boolean hasNpmModule,
            final BuildOptions buildOptions, final List<AnalyzedSource> sources) {
        this.logger = Objects.requireNonNull(logger, "logger");
        this.ownVersion = Objects.requireNonNull(ownVersion, "ownVersion");
        this.hasNpmModule = hasNpmModule;
        this.buildOptions = Objects.requireNonNull(buildOptions, "buildOptions");
        this.workflowGenerator = new CiBuildWorkflowGenerator(this.buildOptions,
                new JavaVersionExtractor(sources).extractVersions());
    }

    List<FileTemplate> getGlobalTemplates(final List<AnalyzedSource> sources) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(new FileTemplateFromResource(".github/workflows/broken_links_checker.yml", REQUIRE_EXACT));
        templates.add(new FileTemplateFromResource(".vscode/settings.json", REQUIRE_EXIST));
        final Optional<AnalyzedSource> mvnRoot = sources.stream().filter(this::isMvnRootProject).findFirst();
        templates.add(new FileTemplateFromResource("templates/gitattributes", ".gitattributes", REQUIRE_EXIST)
                .replacing("pomFiles", mvnRoot.isPresent() ? POM_FILES_GENERATED : ""));
        if (mvnRoot.isPresent()) {
            templates.addAll(getGenericMavenTemplates(sources, mvnRoot.get().getModules()));
        } else {
            templates.addAll(getProjectKeeperVerifyWorkflowTemplates());
            this.logger.warn(ExaError.messageBuilder("W-PK-CORE-91")
                    .message("For this project structure project keeper does not know how to configure ci-build.")
                    .mitigation("Please create the required actions on your own.").toString());
        }
        return templates;
    }

    private List<FileTemplate> getGenericMavenTemplates(final List<AnalyzedSource> sources,
            final Set<ProjectKeeperModule> modules) {
        final List<FileTemplate> templates = new ArrayList<>();
        templates.add(getCiBuildTemplate(modules));
        templates.add(getNextJavaWorkflow(modules, new JavaVersionExtractor(sources).getNextVersion()));
        templates.add(this.workflowGenerator.createDependenciesCheckWorkflow());
        templates.add(this.workflowGenerator.createDependenciesUpdateWorkflow());
        templates.add(this.workflowGenerator.createReleaseWorkflow(sources));
        return templates;
    }

    private FileTemplate getNextJavaWorkflow(final Set<ProjectKeeperModule> modules, final String nextJavaVersion) {
        final FileTemplateFromResource template = new FileTemplateFromResource(
                ".github/workflows/ci-build-next-java.yml", REQUIRE_EXACT) //
                .replacing("skipNativeImage",
                        modules.contains(ProjectKeeperModule.NATIVE_IMAGE) ? "-P skipNativeImage" : "")
                .replacing("nextJavaVersion", nextJavaVersion);
        return new ContentCustomizingTemplate(template,
                new GitHubWorkflowCustomizer(new GitHubWorkflowJavaVersionCustomizer(List.of(nextJavaVersion))));
    }

    private FileTemplate getCiBuildTemplate(final Set<ProjectKeeperModule> modules) {
        if (modules.contains(NATIVE_IMAGE)) {
            return new FileTemplateFromResource("templates/.github/workflows/ci-build-native-build.yml",
                    ".github/workflows/ci-build.yml", REQUIRE_EXACT);
        } else {
            return this.workflowGenerator.createCiBuildWorkflow();
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
            templates.add(new FileTemplateFromResource(".settings/org.eclipse.jdt.core.prefs", REQUIRE_EXACT)
                    .replacing("javaVersion", canonicalize(source.getJavaVersion())));
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

    private String canonicalize(final String version) {
        return "8".equals(version) ? "1.8" : version;
    }

    // [impl -> dsn~pk-verify-workflow~1]
    private List<FileTemplate> getProjectKeeperVerifyWorkflowTemplates() {
        return List.of(projectKeeperVerifyTemplate(), new ProjectKeeperShellScript(this.ownVersion));
    }

    private FileTemplate projectKeeperVerifyTemplate() {
        final String path = ".github/workflows/project-keeper-verify.yml";
        final FileTemplateFromResource template = new FileTemplateFromResource( //
                "non_maven_templates/" + path, path, REQUIRE_EXACT);
        return template.replacing("installNode", String.valueOf(this.hasNpmModule));
    }
}
