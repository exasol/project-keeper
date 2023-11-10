package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.validators.files.FileTemplate.Validation.REQUIRE_EXACT;

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
        return new FileTemplateFromResource(".github/workflows/ci-build.yml", REQUIRE_EXACT)
                .replacing(CI_BUILD_RUNNER_OS_PLACEHOLDER, buildConfig.getRunnerOs())
                .replacing("freeDiskSpace", String.valueOf(buildConfig.shouldFreeDiskSpace()));
    }
}
