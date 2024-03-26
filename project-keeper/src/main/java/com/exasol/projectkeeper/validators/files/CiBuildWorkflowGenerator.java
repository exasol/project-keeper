package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static java.util.stream.Collectors.joining;

import com.exasol.projectkeeper.shared.config.BuildOptions;

class CiBuildWorkflowGenerator {
    private final BuildOptions buildOptions;

    CiBuildWorkflowGenerator(final BuildOptions buildOptions) {
        this.buildOptions = buildOptions;
    }

    FileTemplate createCiBuildWorkflow() {
        final FileTemplateFromResource template = new FileTemplateFromResource(
                "templates/.github/workflows/" + getCiBuildTemplate(), ".github/workflows/ci-build.yml", REQUIRE_EXACT)
                .replacing("ciBuildRunnerOS", buildOptions.getRunnerOs())
                .replacing("freeDiskSpace", String.valueOf(buildOptions.shouldFreeDiskSpace()));

        if (isMatrixBuild()) {
            template.replacing("matrixExasolDbVersions",
                    buildOptions.getExasolDbVersions().stream().map(this::quote).collect(joining(", ")));
            template.replacing("defaultExasolDbVersion", quote(buildOptions.getExasolDbVersions().get(0)));
        }
        return new ContentCustomizingTemplate(template, new GitHubWorkflowStepCustomizer(buildOptions));
    }

    private String quote(final String value) {
        return '"' + value + '"';
    }

    private String getCiBuildTemplate() {
        if (isMatrixBuild()) {
            return "ci-build-db-version-matrix.yml";
        } else {
            return "ci-build.yml";
        }
    }

    private boolean isMatrixBuild() {
        return !buildOptions.getExasolDbVersions().isEmpty();
    }
}
