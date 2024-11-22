package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static java.util.stream.Collectors.joining;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.config.workflow.CustomWorkflow;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class CiBuildWorkflowGenerator {
    private static final String WORKFLOW_PATH = ".github/workflows/";
    private static final String CI_BUILD_WORKFLOW_NAME = "ci-build.yml";
    private static final String CI_BUILD_PATH_IN_PROJECT = WORKFLOW_PATH + CI_BUILD_WORKFLOW_NAME;
    private final BuildOptions buildOptions;
    private final List<String> javaVersions;
    private final Supplier<String> nextJavaVersion;

    CiBuildWorkflowGenerator(final BuildOptions buildOptions, final List<String> javaVersions,
            final Supplier<String> nextJavaVersion) {
        this.buildOptions = Objects.requireNonNull(buildOptions, "buildOptions");
        this.javaVersions = Objects.requireNonNull(javaVersions, "javaVersions");
        this.nextJavaVersion = Objects.requireNonNull(nextJavaVersion, "nextJavaVersion");
    }

    FileTemplate createCiBuildWorkflow() {
        final CiTemplateType buildType = getCiBuildType();
        final String templateResource = "templates/" + WORKFLOW_PATH + buildType.templateName;
        final FileTemplateFromResource template = new FileTemplateFromResource(templateResource,
                CI_BUILD_PATH_IN_PROJECT, REQUIRE_EXACT);
        template.replacing("ciBuildRunnerOS", buildOptions.getRunnerOs());
        template.replacing("freeDiskSpace", String.valueOf(buildOptions.shouldFreeDiskSpace()));
        template.replacing("nextJavaVersion", nextJavaVersion.get());

        if (buildType == CiTemplateType.EXASOL_VERSION_MATRIX) {
            template.replacing("matrixExasolDbVersions",
                    buildOptions.getExasolDbVersions().stream().map(this::quote).collect(joining(", ")));
            template.replacing("defaultExasolDbVersion", quote(buildOptions.getExasolDbVersions().get(0)));
        }
        final String buildJobId = buildType.buildJobId;
        return createTemplate(template, CI_BUILD_WORKFLOW_NAME, buildJobId);
    }

    private FileTemplate createTemplate(final FileTemplateFromResource template, final String workflowName,
            final String buildJobId) {
        final Optional<CustomWorkflow> workflow = buildOptions.getWorkflow(workflowName);
        final List<StepCustomization> customizations = workflow.map(CustomWorkflow::getSteps)
                .orElseGet(Collections::emptyList);
        final String environmentName = workflow.map(CustomWorkflow::getEnvironment).orElse(null);
        return new ContentCustomizingTemplate(template, new GitHubWorkflowCustomizer( //
                javaVersionCustomizer(),
                // [impl->dsn~customize-build-process.ci-build~0]
                new GitHubWorkflowStepCustomizer(workflowName, customizations),
                // [impl->dsn~customize-build-process.ci-build.environment~1]
                new GitHubWorkflowEnvironmentCustomizer(buildJobId, environmentName)));
    }

    private GitHubWorkflowJavaVersionCustomizer javaVersionCustomizer() {
        return new GitHubWorkflowJavaVersionCustomizer(javaVersions, nextJavaVersion.get());
    }

    private List<StepCustomization> findCustomizations(final String workflowName) {
        return buildOptions.getWorkflow(workflowName) //
                .map(CustomWorkflow::getSteps) //
                .orElseGet(Collections::emptyList);
    }

    private String quote(final String value) {
        return '"' + value + '"';
    }

    private CiTemplateType getCiBuildType() {
        if (!buildOptions.getExasolDbVersions().isEmpty()) {
            return CiTemplateType.EXASOL_VERSION_MATRIX;
        }
        return CiTemplateType.DEFAULT;
    }

    // [impl->dsn~release-workflow.deploy-maven-central~1]
    // [impl->dsn~customize-build-process.release~0]
    FileTemplate createReleaseWorkflow(final List<AnalyzedSource> sources) {
        final Consumer<FileTemplateFromResource> templateCustomizer = template -> template
                .replacing("mavenCentralDeployment", mavenCentralDeploymentRequired(sources) ? "true" : "false");
        return createCustomizedWorkflow("release.yml", templateCustomizer);
    }

    private boolean mavenCentralDeploymentRequired(final List<AnalyzedSource> sources) {
        return sources.stream() //
                .map(AnalyzedSource::getModules) //
                .filter(Objects::nonNull) //
                .flatMap(Set::stream) //
                .anyMatch(ProjectKeeperModule.MAVEN_CENTRAL::equals);
    }

    // [impl->dsn~customize-build-process.dependency-check~0]
    FileTemplate createDependenciesCheckWorkflow() {
        return createCustomizedWorkflow("dependencies_check.yml");
    }

    // [impl->dsn~dependency-updater.workflow.generate~1]
    // [impl->dsn~customize-build-process.dependency-update~0]
    FileTemplate createDependenciesUpdateWorkflow() {
        return createCustomizedWorkflow("dependencies_update.yml");
    }

    private FileTemplate createCustomizedWorkflow(final String workflowName) {
        return createCustomizedWorkflow(workflowName, template -> {
        });
    }

    private FileTemplate createCustomizedWorkflow(final String workflowName,
            final Consumer<FileTemplateFromResource> templateCustomizer) {
        final FileTemplateFromResource template = new FileTemplateFromResource(WORKFLOW_PATH + workflowName,
                REQUIRE_EXACT);
        templateCustomizer.accept(template);
        final List<StepCustomization> customizations = findCustomizations(workflowName);
        return new ContentCustomizingTemplate(template, new GitHubWorkflowCustomizer(javaVersionCustomizer(),
                new GitHubWorkflowStepCustomizer(workflowName, customizations)));
    }

    enum CiTemplateType {
        DEFAULT(CI_BUILD_WORKFLOW_NAME, "build-and-test"),
        EXASOL_VERSION_MATRIX("ci-build-db-version-matrix.yml", "matrix-build");

        private final String templateName;
        private final String buildJobId;

        private CiTemplateType(final String templateName, final String buildJobId) {
            this.templateName = templateName;
            this.buildJobId = buildJobId;
        }
    }
}
