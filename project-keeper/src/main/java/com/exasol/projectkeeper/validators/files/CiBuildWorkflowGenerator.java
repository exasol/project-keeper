package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static java.util.stream.Collectors.joining;

import java.util.*;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.ProjectKeeperModule;
import com.exasol.projectkeeper.shared.config.workflow.CustomWorkflow;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization;
import com.exasol.projectkeeper.sources.AnalyzedSource;

class CiBuildWorkflowGenerator {
    private static final String CI_BUILD_WORKFLOW_NAME = "ci-build.yml";
    private static final String CI_BUILD_PATH_IN_PROJECT = ".github/workflows/" + CI_BUILD_WORKFLOW_NAME;
    private final BuildOptions buildOptions;

    CiBuildWorkflowGenerator(final BuildOptions buildOptions) {
        this.buildOptions = buildOptions;
    }

    FileTemplate createCiBuildWorkflow() {
        final CiTemplateType buildType = getCiBuildType();
        final String templateResource = "templates/.github/workflows/" + buildType.templateName;
        final FileTemplateFromResource template = new FileTemplateFromResource(templateResource,
                CI_BUILD_PATH_IN_PROJECT, REQUIRE_EXACT);
        template.replacing("ciBuildRunnerOS", buildOptions.getRunnerOs());
        template.replacing("freeDiskSpace", String.valueOf(buildOptions.shouldFreeDiskSpace()));

        if (buildType == CiTemplateType.EXASOL_VERSION_MATRIX) {
            template.replacing("matrixExasolDbVersions",
                    buildOptions.getExasolDbVersions().stream().map(this::quote).collect(joining(", ")));
            template.replacing("defaultExasolDbVersion", quote(buildOptions.getExasolDbVersions().get(0)));
        }
        // [impl->dsn~customize-build-process.ci-build~0]
        return new ContentCustomizingTemplate(template,
                new GitHubWorkflowStepCustomizer(findCustomizations(CI_BUILD_WORKFLOW_NAME), buildType.buildJobId));
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
        final String workflowName = "release.yml";
        final FileTemplate template = new FileTemplateFromResource(".github/workflows/" + workflowName, REQUIRE_EXACT)
                .replacing("mavenCentralDeployment", mavenCentralDeploymentRequired(sources) ? "true" : "false");
        return new ContentCustomizingTemplate(template,
                new GitHubWorkflowStepCustomizer(findCustomizations(workflowName), "release"));
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
        final String workflowName = "dependencies_check.yml";
        return new ContentCustomizingTemplate(
                new FileTemplateFromResource(".github/workflows/" + workflowName, REQUIRE_EXACT),
                new GitHubWorkflowStepCustomizer(findCustomizations(workflowName), "report_security_issues"));
    }

    // [impl->dsn~dependency-updater.workflow.generate~1]
    // [impl->dsn~customize-build-process.dependency-update~0]
    FileTemplate createDependenciesUpdateWorkflow() {
        final String workflowName = "dependencies_update.yml";
        return new ContentCustomizingTemplate(
                new FileTemplateFromResource(".github/workflows/" + workflowName, REQUIRE_EXACT),
                new GitHubWorkflowStepCustomizer(findCustomizations(workflowName), "update_dependencies"));
    }

    enum CiTemplateType {
        DEFAULT(CI_BUILD_WORKFLOW_NAME, "build"),
        EXASOL_VERSION_MATRIX("ci-build-db-version-matrix.yml", "matrix-build");

        private final String templateName;
        private final String buildJobId;

        private CiTemplateType(final String templateName, final String buildJobId) {
            this.templateName = templateName;
            this.buildJobId = buildJobId;
        }
    }
}
