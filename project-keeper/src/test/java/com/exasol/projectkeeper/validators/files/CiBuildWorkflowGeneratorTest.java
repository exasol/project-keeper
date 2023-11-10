package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

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
