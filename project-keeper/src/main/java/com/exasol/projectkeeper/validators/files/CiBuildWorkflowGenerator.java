package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static java.util.stream.Collectors.joining;

import com.exasol.projectkeeper.shared.config.BuildOptions;

class CiBuildWorkflowGenerator {
    private static final String PATH_IN_PROJECT = ".github/workflows/ci-build.yml";
    private final BuildOptions buildOptions;

    CiBuildWorkflowGenerator(final BuildOptions buildOptions) {
        this.buildOptions = buildOptions;
    }

    FileTemplate createCiBuildWorkflow() {
        final CiTemplateType buildType = getCiBuildType();
        final String templateResource = "templates/.github/workflows/" + buildType.templateName;
        final FileTemplateFromResource template = new FileTemplateFromResource(templateResource, PATH_IN_PROJECT,
                REQUIRE_EXACT);
        template.replacing("ciBuildRunnerOS", buildOptions.getRunnerOs());
        template.replacing("freeDiskSpace", String.valueOf(buildOptions.shouldFreeDiskSpace()));

        if (buildType == CiTemplateType.EXASOL_VERSION_MATRIX) {
            template.replacing("matrixExasolDbVersions",
                    buildOptions.getExasolDbVersions().stream().map(this::quote).collect(joining(", ")));
            template.replacing("defaultExasolDbVersion", quote(buildOptions.getExasolDbVersions().get(0)));
        }
        return new ContentCustomizingTemplate(template,
                new GitHubWorkflowStepCustomizer(buildOptions, buildType.buildJobId));
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

    enum CiTemplateType {
        DEFAULT("ci-build.yml", "build"), EXASOL_VERSION_MATRIX("ci-build-db-version-matrix.yml", "matrix-build");

        private final String templateName;
        private final String buildJobId;

        private CiTemplateType(final String templateName, final String buildJobId) {
            this.templateName = templateName;
            this.buildJobId = buildJobId;
        }
    }
}
