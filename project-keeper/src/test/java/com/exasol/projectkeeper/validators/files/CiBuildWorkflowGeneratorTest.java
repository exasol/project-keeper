package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildConfig;

class CiBuildWorkflowGeneratorTest {

    @Test
    void rdOrgChecksumContainsRunnerOs() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().runnerOs("my-runner-os")),
                containsString("runs-on: my-runner-os"));
    }

    @Test
    void rdOrgChecksumDoesNotFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().freeDiskSpace(false)),
                containsString("- name: Free Disk Space\n" + //
                        "        if: ${{ false }}"));
    }

    @Test
    void rdOrgChecksumDoesFreeDiskSpace() {
        assertThat(releaseDroidOriginalChecksumContent(BuildConfig.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space\n" + //
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
                containsString("- name: Free Disk Space\n" + //
                        "        if: ${{ false }}"));
    }

    @Test
    void ciBuildDoesFreeDiskSpace() {
        assertThat(ciBuildContent(BuildConfig.builder().freeDiskSpace(true)),
                containsString("- name: Free Disk Space\n" + //
                        "        if: ${{ true }}"));
    }

    @Test
    void ciBuildNonMatrixBuild() {
        assertThat(ciBuildContent(BuildConfig.builder()), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}\n"), //
                not(containsString("matrix")), //
                not(containsString("com.exasol.dockerdb.image")) //
        ));
    }

    @Test
    void ciBuildMatrixBuild() {
        assertThat(ciBuildContent(BuildConfig.builder().exasolDbVersions(List.of("v1", "v2"))), allOf( //
                containsString("group: ${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}\n"), //
                containsString("matrix:\n        exasol_db_version: [\"v1\", \"v2\"]"), //
                containsString("env:\n      DEFAULT_EXASOL_DB_VERSION: \"v1\""),
                containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}"), //
                containsString(
                        "- name: Sonar analysis\n        if: ${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}") //
        ));
    }

    private String releaseDroidOriginalChecksumContent(final BuildConfig.Builder configBuilder) {
        final String content = testee(configBuilder).createReleaseDroidPrepareOriginalChecksumWorkflow().getContent();
        validateYamlSyntax(content);
        return content;
    }

    private String ciBuildContent(final BuildConfig.Builder configBuilder) {
        final String content = testee(configBuilder).createCiBuildWorkflow().getContent();
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
