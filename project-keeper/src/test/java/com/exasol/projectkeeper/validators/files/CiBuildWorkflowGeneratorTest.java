package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.validators.files.FileTemplate.Validation;

class CiBuildWorkflowGeneratorTest {

    private static String NL = System.lineSeparator();

    @Test
    void rdOrgChecksumContainsRunnerOs() {
        assertThat(releaseDroidOriginalChecksumContent(BuildOptions.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @Test
    void rdOrgChecksumDoesNotFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildOptions.builder().freeDiskSpace(false)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ false }}"));
    }

    @Test
    void rdOrgChecksumDoesFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildOptions.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ true }}"));
    }

    @Test
    void ciBuildContainsRunnerOs() {
        assertThat(ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @Test
    void ciBuildDoesNotFreeDiskSpace() {
        assertThat(ciBuildContent(BuildOptions.builder().freeDiskSpace(false)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ false }}"));
    }

    @Test
    void ciBuildDoesFreeDiskSpace() {
        assertThat(ciBuildContent(BuildOptions.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ true }}"));
    }

    @Test
    void ciBuildNonMatrixBuild() {
        assertThat(ciBuildContent(BuildOptions.builder()), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}" + NL), //
                not(containsString("matrix")), //
                not(containsString("com.exasol.dockerdb.image")) //
        ));
    }

    @Test
    void ciBuildMatrixBuild() {
        assertThat(ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1", "v2"))), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}" + NL), //
                containsString("matrix:" + NL + "        exasol_db_version: [\"v1\", \"v2\"]"), //
                containsString("env:" + NL + "      DEFAULT_EXASOL_DB_VERSION: \"v1\""),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString("- name: Sonar analysis" + NL
                        + "        if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}") //
        ));
    }

    @Test
    void ciBuildMatrixBuildSingleVersion() {
        assertThat(ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1"))), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}" + NL), //
                containsString("matrix:" + NL + "        exasol_db_version: [\"v1\"]"), //
                containsString("env:" + NL + "      DEFAULT_EXASOL_DB_VERSION: \"v1\""),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString("- name: Sonar analysis" + NL
                        + "        if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}") //
        ));
    }

    private String releaseDroidOriginalChecksumContent(final BuildOptions.Builder optionsBuilder) {
        final FileTemplate template = testee(optionsBuilder).createReleaseDroidPrepareOriginalChecksumWorkflow();
        final String content = template.getContent();
        assertAll(() -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> assertThat(template.getPathInProject(),
                        equalTo(Path.of(".github/workflows/release_droid_prepare_original_checksum.yml"))),
                () -> validateYamlSyntax(content));
        return content;
    }

    private String ciBuildContent(final BuildOptions.Builder optionsBuilder) {
        final FileTemplateFromResource template = testee(optionsBuilder).createCiBuildWorkflow();
        final String content = template.getContent();
        assertAll(() -> assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/ci-build.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return content;
    }

    private void validateYamlSyntax(final String content) {
        final Yaml yaml = new Yaml();
        assertDoesNotThrow(() -> yaml.load(content));
    }

    private CiBuildWorkflowGenerator testee(final BuildOptions.Builder optionsBuilder) {
        return new CiBuildWorkflowGenerator(optionsBuilder.build());
    }
}
