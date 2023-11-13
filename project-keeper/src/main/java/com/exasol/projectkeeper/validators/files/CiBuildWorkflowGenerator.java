package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;
import static java.util.stream.Collectors.joining;

import com.exasol.projectkeeper.shared.config.BuildConfig;

class CiBuildWorkflowGenerator {
    private static final String CI_BUILD_RUNNER_OS_PLACEHOLDER = "ciBuildRunnerOS";
    private final BuildConfig buildConfig;

    CiBuildWorkflowGenerator(final BuildConfig buildConfig) {
        this.buildConfig = buildConfig;
    }

    FileTemplate createReleaseDroidPrepareOriginalChecksumWorkflow() {
        return new FileTemplateFromResource(".github/workflows/release_droid_prepare_original_checksum.yml",
                REQUIRE_EXACT) //
                .replacing(CI_BUILD_RUNNER_OS_PLACEHOLDER, buildConfig.getRunnerOs())
                .replacing("freeDiskSpace", String.valueOf(buildConfig.shouldFreeDiskSpace()));
    }

    FileTemplateFromResource createCiBuildWorkflow() {
        final FileTemplateFromResource template = new FileTemplateFromResource(getCiBuildTemplate(), REQUIRE_EXACT)
                .replacing(CI_BUILD_RUNNER_OS_PLACEHOLDER, buildConfig.getRunnerOs())
                .replacing("freeDiskSpace", String.valueOf(buildConfig.shouldFreeDiskSpace()));

        if (isMatrixBuild()) {
            template.replacing("matrixExasolDbVersions",
                    buildConfig.getExasolDbVersions().stream().map(this::quote).collect(joining(", ")));
            template.replacing("defaultExasolDbVersion", quote(buildConfig.getExasolDbVersions().get(0)));
        }
        return template;
    }

    private String quote(final String value) {
        return '"' + value + '"';
    }

    private String getCiBuildTemplate() {
        if (isMatrixBuild()) {
            return ".github/workflows/ci-build-db-version-matrix.yml";
        } else {
            return ".github/workflows/ci-build.yml";
        }
    }

    private boolean isMatrixBuild() {
        return !buildConfig.getExasolDbVersions().isEmpty();
    }
}
