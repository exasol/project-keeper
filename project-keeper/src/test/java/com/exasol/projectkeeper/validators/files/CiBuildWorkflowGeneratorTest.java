package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildConfig;

class CiBuildWorkflowGeneratorTest {

    private static String NL = System.lineSeparator();

    @Test
    void rdOrgChecksumContainsRunnerOs() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @Test
    void rdOrgChecksumDoesNotFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().freeDiskSpace(false)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ false }}"));
    }

    @Test
    void rdOrgChecksumDoesFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ true }}"));
    }

    @Test
    void ciBuildContainsRunnerOs() {
        assertThat(ciBuildContent(BuildConfig.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @Test
    void ciBuildDoesNotFreeDiskSpace() {
        assertThat(ciBuildContent(BuildConfig.builder().freeDiskSpace(false)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ false }}"));
    }

    @Test
    void ciBuildDoesFreeDiskSpace() {
        assertThat(ciBuildContent(BuildConfig.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space" + NL + //
                        "        if: ${{ true }}"));
    }

    @Test
    void ciBuildNonMatrixBuild() {
        assertThat(ciBuildContent(BuildConfig.builder()), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}" + NL), //
                not(containsString("matrix")), //
                not(containsString("com.exasol.dockerdb.image")) //
        ));
    }

    @Test
    void ciBuildMatrixBuild() {
        assertThat(ciBuildContent(BuildConfig.builder().exasolDbVersions(List.of("v1", "v2"))), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}" + NL), //
                containsString("matrix:" + NL + "        exasol_db_version: [\"v1\", \"v2\"]"), //
                containsString("env:" + NL + "      DEFAULT_EXASOL_DB_VERSION: \"v1\""),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString("- name: Sonar analysis" + NL
                        + "        if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}") //
        ));
    }

    private String releaseDroidOriginalChecksumContent(final BuildConfig.Builder configBuilder) {
        final FileTemplate template = testee(configBuilder).createReleaseDroidPrepareOriginalChecksumWorkflow();
        assertThat(template.getPathInProject(),
                equalTo(Path.of(".github/workflows/release_droid_prepare_original_checksum.yml")));
        final String content = template.getContent();
        validateYamlSyntax(content);
        return content;
    }

    private String ciBuildContent(final BuildConfig.Builder configBuilder) {
        final FileTemplateFromResource template = testee(configBuilder).createCiBuildWorkflow();
        assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/ci-build.yml")));
        final String content = template.getContent();
        validateYamlSyntax(content);
        return content;
    }

    private void validateYamlSyntax(final String content) {
        new Yaml().load(content);
    }

    private CiBuildWorkflowGenerator testee(final BuildConfig.Builder configBuilder) {
        return new CiBuildWorkflowGenerator(configBuilder.build());
    }
}
