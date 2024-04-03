package com.exasol.projectkeeper.validators.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.workflow.*;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization.Type;
import com.exasol.projectkeeper.validators.files.FileTemplate.Validation;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Job;
import com.exasol.projectkeeper.validators.files.GitHubWorkflow.Step;

class CiBuildWorkflowGeneratorTest {

    @Test
    void doesNotModifyOnTrigger() {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os"));
        assertThat(workflow.getOnTrigger(), hasKey("push"));
    }

    @Test
    void defaultRunnerOs() {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder());
        assertThat(workflow.getJob("build").getRunnerOS(), equalTo("ubuntu-latest"));
    }

    @Test
    void customRunnerOs() {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os"));
        assertThat(workflow.getJob("build").getRunnerOS(), equalTo("my-runner-os"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void ciBuildFreeDiskSpace(final boolean freeDiskSpace) {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder().freeDiskSpace(freeDiskSpace));
        assertThat(workflow.getJob("build").getStep("free-disk-space").getIfCondition(),
                equalTo("${{ " + freeDiskSpace + " }}"));
    }

    // [utest->dsn~customize-build-process.ci-build~0]
    @Test
    void customizeBuildSteps() {
        final Job job = ciBuildContent(BuildOptions.builder().workflows(List.of(CustomWorkflow.builder()
                .workflowName("ci-build.yml")
                .addStep(StepCustomization.builder().type(Type.INSERT_AFTER).stepId("sonar-analysis")
                        .step(WorkflowStep.createStep(Map.of("id", "inserted-step", "name", "Inserted Step"))).build())
                .build()))).getJob("build");
        final List<String> stepIds = job.getSteps().stream().map(Step::getId).toList();
        assertAll(() -> assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10))),
                () -> assertThat(job.getStep("inserted-step").getName(), equalTo("Inserted Step")),
                () -> assertThat(stepIds,
                        contains("free-disk-space", "checkout", "setup-java", "cache-sonar",
                                "enable-testcontainer-reuse", "build-pk-verify", "sonar-analysis", "inserted-step",
                                "verify-release-artifacts", "upload-artifacts", "check-release")));
    }

    // [utest->dsn~customize-build-process.ci-build~0]
    @Test
    void customizeBuildStepsMatrixBuild() {
        final Job job = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1", "v2"))
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml")
                        .addStep(StepCustomization.builder().type(Type.INSERT_AFTER).stepId("sonar-analysis")
                                .step(WorkflowStep.createStep(Map.of("id", "inserted-step", "name", "Inserted Step")))
                                .build())
                        .build())))
                .getJob("matrix-build");
        final List<String> stepIds = job.getSteps().stream().map(Step::getId).toList();
        assertAll(() -> assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10))),
                () -> assertThat(job.getStep("inserted-step").getName(), equalTo("Inserted Step")),
                () -> assertThat(stepIds,
                        contains("free-disk-space", "checkout", "setup-java", "cache-sonar",
                                "enable-testcontainer-reuse", "build-pk-verify", "sonar-analysis", "inserted-step",
                                "verify-release-artifacts", "upload-artifacts")));
    }

    @Test
    void ciBuildNonMatrixBuild() {
        final Job job = ciBuildContent(BuildOptions.builder()).getJob("build");
        assertAll(
                () -> assertThat(job.getConcurrency(),
                        equalTo(Map.of("group", "${{ github.workflow }}-${{ github.ref }}", "cancel-in-progress",
                                true))),
                () -> assertThat(job.getStrategy(), nullValue()), () -> assertThat(job.getEnv(), nullValue()),
                () -> assertThat(job.getStep("build-pk-verify").getRunCommand(),
                        not(containsString("-Dcom.exasol.dockerdb.image"))),
                () -> assertThat(job.getStep("sonar-analysis").getIfCondition(),
                        equalTo("${{ env.SONAR_TOKEN != null }}")));
    }

    @Test
    void ciBuildMatrixBuild() {
        final Job matrixJob = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1", "v2")))
                .getJob("matrix-build");
        assertAll(() -> assertThat(matrixJob.getConcurrency(),
                equalTo(Map.of("group", "${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}",
                        "cancel-in-progress", true))),
                () -> assertThat(matrixJob.getStrategy().get("matrix"),
                        equalTo(Map.of("exasol_db_version", List.of("v1", "v2")))),
                () -> assertThat(matrixJob.getEnv(), equalTo(Map.of("DEFAULT_EXASOL_DB_VERSION", "v1"))),
                () -> assertThat(matrixJob.getStep("build-pk-verify").getRunCommand(),
                        containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}")),
                () -> assertThat(matrixJob.getStep("sonar-analysis").getIfCondition(), equalTo(
                        "${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}")));
    }

    @Test
    void ciBuildMatrixBuildSingleVersion() {
        final Job matrixJob = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1")))
                .getJob("matrix-build");
        assertAll(() -> assertThat(matrixJob.getConcurrency(),
                equalTo(Map.of("group", "${{ github.workflow }}-${{ github.ref }}-${{ matrix.exasol_db_version }}",
                        "cancel-in-progress", true))),
                () -> assertThat(matrixJob.getStrategy().get("matrix"),
                        equalTo(Map.of("exasol_db_version", List.of("v1")))),
                () -> assertThat(matrixJob.getEnv(), equalTo(Map.of("DEFAULT_EXASOL_DB_VERSION", "v1"))),
                () -> assertThat(matrixJob.getStep("build-pk-verify").getRunCommand(),
                        containsString("-Dcom.exasol.dockerdb.image=${{ matrix.exasol_db_version }}")),
                () -> assertThat(matrixJob.getStep("sonar-analysis").getIfCondition(), equalTo(
                        "${{ env.SONAR_TOKEN != null && matrix.exasol_db_version == env.DEFAULT_EXASOL_DB_VERSION }}")));
    }

    @Test
    void ciBuildMatrixBuildAllStepsHaveId() {
        final Job job = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1"))).getJob("matrix-build");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(9)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void ciBuildBuildAllStepsHaveId() {
        final Job job = ciBuildContent(BuildOptions.builder()).getJob("build");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void releaseAllStepsHaveId() {

    }

    private GitHubWorkflow ciBuildContent(final BuildOptions.Builder optionsBuilder) {
        return ciBuildContent(optionsBuilder.build());
    }

    private GitHubWorkflow ciBuildContent(final BuildOptions options) {
        final FileTemplate template = testee(options).createCiBuildWorkflow();
        final String content = template.getContent();
        assertAll(() -> assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/ci-build.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return parse(content);
    }

    private void validateYamlSyntax(final String content) {
        final Yaml yaml = new Yaml();
        assertDoesNotThrow(() -> yaml.load(content));
    }

    private CiBuildWorkflowGenerator testee(final BuildOptions options) {
        return new CiBuildWorkflowGenerator(options);
    }

    private GitHubWorkflow parse(final String yaml) {
        return GitHubWorkflowIO.create().loadWorkflow(yaml);
    }
}
