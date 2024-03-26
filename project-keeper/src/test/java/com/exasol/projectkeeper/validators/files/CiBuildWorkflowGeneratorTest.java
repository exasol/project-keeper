package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.validators.files.FileTemplate.Validation;

class CiBuildWorkflowGeneratorTest {

    private static String NL = System.lineSeparator();

    @Test
    void doesNotModifyOnTrigger() {
        assertThat(ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os")), containsString("""
                'on':
                  push:
                    branches: [main]
                """));
    }

    @Test
    void ciBuildContainsRunnerOs() {
        assertThat(ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void ciBuildFreeDiskSpace(final boolean freeDiskSpace) {
        assertThat(ciBuildContent(BuildOptions.builder().freeDiskSpace(freeDiskSpace)),
                containsString("- name: Free Disk Space" + NL + //
                        "      if: ${{ " + freeDiskSpace + " }}"));
    }

    @Test
    void ciBuildNonMatrixBuild() {
        assertThat(ciBuildContent(BuildOptions.builder()), allOf( //
                containsString(
                        "concurrency: {group: '${{ github.workflow }}-${{ github.ref }}', cancel-in-progress: true}"), //
                not(containsString("matrix")), //
                not(containsString("com.exasol.dockerdb.image")) //
        ));
    }

    @Test
    void ciBuildMatrixBuild() {
        assertThat(ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1", "v2"))), allOf( //
                containsString(
                        "concurrency: {group: '${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version\n"
                                + "        }}', cancel-in-progress: true}"), //
                containsString("matrix:\n" + //
                        "        exasol_db_version: [v1, v2]\n"), //
                containsString("env: {DEFAULT_EXASOL_DB_VERSION: v1}"),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString("    - name: Sonar analysis\n"
                        + "      if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION\n"
                        + "        }}") //
        ));
    }

    @Test
    void ciBuildMatrixBuildSingleVersion() {
        assertThat(ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1"))), allOf( //
                containsString(
                        "concurrency: {group: '${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version\n"
                                + "        }}', cancel-in-progress: true}"), //
                containsString("matrix:\n        exasol_db_version: [v1]\n"), //
                containsString("env: {DEFAULT_EXASOL_DB_VERSION: v1}"),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString("- name: Sonar analysis\n"
                        + "      if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION\n"
                        + "        }}") //
        ));
    }

    private String ciBuildContent(final BuildOptions.Builder optionsBuilder) {
        return ciBuildContent(optionsBuilder.build());
    }

    private String ciBuildContent(final BuildOptions options) {
        final FileTemplate template = testee(options).createCiBuildWorkflow();
        final String content = template.getContent();
        assertAll(() -> assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/ci-build.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        System.out.println(content);
        return content;
    }

    private void validateYamlSyntax(final String content) {
        final Yaml yaml = new Yaml();
        assertDoesNotThrow(() -> yaml.load(content));
    }

    private CiBuildWorkflowGenerator testee(final BuildOptions options) {
        return new CiBuildWorkflowGenerator(options);
    }
}
