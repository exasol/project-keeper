package com.exasol.projectkeeper.validators.files;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.MAVEN_CENTRAL;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.yaml.snakeyaml.Yaml;

import com.exasol.projectkeeper.shared.config.BuildOptions;
import com.exasol.projectkeeper.shared.config.workflow.*;
import com.exasol.projectkeeper.shared.config.workflow.JobPermissions.AccessLevel;
import com.exasol.projectkeeper.shared.config.workflow.StepCustomization.Type;
import com.exasol.projectkeeper.sources.AnalyzedMavenSource;
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
        assertThat(workflow.getJob("build-and-test").getRunnerOS(), equalTo("ubuntu-latest"));
    }

    @Test
    void customRunnerOs() {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder().runnerOs("my-runner-os"));
        assertThat(workflow.getJob("build-and-test").getRunnerOS(), equalTo("my-runner-os"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void ciBuildFreeDiskSpace(final boolean freeDiskSpace) {
        final GitHubWorkflow workflow = ciBuildContent(BuildOptions.builder().freeDiskSpace(freeDiskSpace));
        assertThat(workflow.getJob("build-and-test").getStep("free-disk-space").getIfCondition(),
                equalTo("${{ " + freeDiskSpace + " }}"));
    }

    // [utest->dsn~customize-build-process.ci-build~0]
    @Test
    void customizeBuildStepsNonMatrixBuild() {
        final Job job = ciBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml").addStep(StepCustomization
                        .builder().jobId("build-and-test").type(Type.INSERT_AFTER).stepId("sonar-analysis")
                        .step(WorkflowStep.createStep(Map.of("id", "inserted-step", "name", "Inserted Step"))).build())
                        .build())))
                .getJob("build-and-test");
        final List<String> stepIds = job.getSteps().stream().map(Step::getId).toList();
        assertAll(() -> assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10))),
                () -> assertThat(job.getStep("inserted-step").getName(), equalTo("Inserted Step")),
                () -> assertThat(stepIds, contains("free-disk-space", "checkout", "setup-java", "cache-sonar",
                        "enable-testcontainer-reuse", "build-pk-verify", "sonar-analysis", "inserted-step",
                        "verify-release-artifacts", "upload-artifacts", "configure-link-check", "run-link-check")));
    }

    // [utest->dsn~customize-build-process.ci-build~0]
    @Test
    void customizeBuildStepsMatrixBuild() {
        final Job job = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1", "v2"))
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml").addStep(StepCustomization
                        .builder().jobId("matrix-build").type(Type.INSERT_AFTER).stepId("sonar-analysis")
                        .step(WorkflowStep.createStep(Map.of("id", "inserted-step", "name", "Inserted Step"))).build())
                        .build())))
                .getJob("matrix-build");
        final List<String> stepIds = job.getSteps().stream().map(Step::getId).toList();
        assertAll(() -> assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10))),
                () -> assertThat(job.getStep("inserted-step").getName(), equalTo("Inserted Step")),
                () -> assertThat(stepIds, contains("free-disk-space", "checkout", "setup-java", "cache-sonar",
                        "enable-testcontainer-reuse", "build-pk-verify", "sonar-analysis", "inserted-step",
                        "verify-release-artifacts", "upload-artifacts", "configure-link-check", "run-link-check")));
    }

    @Test
    void ciBuildContentNonMatrixBuild() {
        final Job buildJob = ciBuildContent(BuildOptions.builder()).getJob("build-and-test");
        final Job nextJavaJob = ciBuildContent(BuildOptions.builder()).getJob("next-java-compatibility");
        assertAll(
                () -> assertThat(buildJob.getConcurrency(),
                        equalTo(Map.of("group", "${{ github.workflow }}-build-and-test-${{ github.ref }}",
                                "cancel-in-progress", true))),
                () -> assertThat(nextJavaJob.getConcurrency(),
                        equalTo(Map.of("group", "${{ github.workflow }}-next-java-${{ github.ref }}",
                                "cancel-in-progress", true))),
                () -> assertThat(buildJob.getStrategy(), nullValue()), () -> assertThat(buildJob.getEnv(), nullValue()),
                () -> assertThat(buildJob.getStep("build-pk-verify").getRunCommand(),
                        not(containsString("-Dcom.exasol.dockerdb.image"))),
                () -> assertThat(buildJob.getStep("sonar-analysis").getIfCondition(),
                        equalTo("${{ env.SONAR_TOKEN != null }}")));
    }

    @Test
    void ciBuildContentMatrixBuild() {
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

    // [utest->dsn~customize-build-process.ci-build.environment~1]
    @Test
    void ciBuildCustomEnvironment() {
        final Job job = ciBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml").environment("aws").build())))
                .getJob("build-and-test");
        assertThat(job.getEnvironment(), equalTo("aws"));
    }

    @Test
    void ciBuildDefaultPermissions() {
        final Job job = ciBuildContent(BuildOptions.builder()).getJob("build-and-test");
        assertAll(
                () -> assertThat(job.getPermissions(), aMapWithSize(2)),
                () -> assertThat(job.getPermissions(), hasEntry("contents", "read")),
                () -> assertThat(job.getPermissions(), hasEntry("issues", "read")));
    }

    // [utest->dsn~customize-build-process.job-permissions~0]
    @Test
    void ciBuildCustomPermissions() {
        final Job job = ciBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml")
                        .jobs(List.of(CustomJob.builder().jobName("build-and-test")
                                .permissions(JobPermissions.builder().add("custom", AccessLevel.NONE)
                                        .add("other", AccessLevel.WRITE).build())
                                .build()))
                        .build())))
                .getJob("build-and-test");
        assertAll(
                () -> assertThat(job.getPermissions(), aMapWithSize(2)),
                () -> assertThat(job.getPermissions(), hasEntry("custom", "none")),
                () -> assertThat(job.getPermissions(), hasEntry("other", "write")));
    }

    @Test
    void ciBuildDefaultHasNoEnvironment() {
        final Job job = ciBuildContent(BuildOptions.builder()).getJob("build-and-test");
        assertThat(job.getEnvironment(), nullValue());
    }

    // [utest->dsn~release-workflow.deploy-maven-central~1]
    @Test
    void releaseBuildWithMavenRelease() {
        final Job job = releaseBuildContent(BuildOptions.builder().build(),
                AnalyzedMavenSource.builder().modules(Set.of(MAVEN_CENTRAL)).build()).getJob("release");
        assertAll( //
                () -> assertThat(job.getSteps(), hasSize(13)),
                () -> assertThat(job.getStep("configure-maven-central-credentials").getIfCondition(),
                        equalTo("${{ true }}")),
                () -> assertThat(job.getStep("setup-jdks").getIfCondition(), equalTo("${{ ! true }}")),
                () -> assertThat(job.getStep("list-secret-gpg-keys").getIfCondition(),
                        equalTo("${{ true && (! inputs.skip-maven-central) }}")),
                () -> assertThat(job.getStep("deploy-maven-central").getIfCondition(),
                        equalTo("${{ true && (! inputs.skip-maven-central) }}")));
    }

    @Test
    void releaseBuildWithoutMavenRelease() {
        final Job job = releaseBuildContent(BuildOptions.builder().build(), AnalyzedMavenSource.builder().build())
                .getJob("release");
        assertAll( //
                () -> assertThat(job.getSteps(), hasSize(13)),
                () -> assertThat(job.getStep("configure-maven-central-credentials").getIfCondition(),
                        equalTo("${{ false }}")),
                () -> assertThat(job.getStep("setup-jdks").getIfCondition(), equalTo("${{ ! false }}")),
                () -> assertThat(job.getStep("list-secret-gpg-keys").getIfCondition(),
                        equalTo("${{ false && (! inputs.skip-maven-central) }}")),
                () -> assertThat(job.getStep("deploy-maven-central").getIfCondition(),
                        equalTo("${{ false && (! inputs.skip-maven-central) }}")));
    }

    // [utest->dsn~customize-build-process.release~0]
    @Test
    void releaseBuildCustomized() {
        final Job job = releaseBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("release.yml")
                        .addStep(StepCustomization.builder().jobId("release").type(Type.INSERT_AFTER).stepId("build")
                                .step(WorkflowStep.createStep(Map.of("id", "new-step", "name", "New Step"))).build())
                        .build()))
                .build()).getJob("release");
        assertAll(() -> assertThat(job.getSteps(), hasSize(14)),
                () -> assertThat(job.getStep("new-step").getName(), equalTo("New Step")));
    }

    // [utest->dsn~customize-build-process.dependency-check~0]
    @Test
    void dependencyCheckBuildCustomized() {
        final Job job = dependencyCheckBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("dependencies_check.yml")
                        .addStep(StepCustomization.builder().jobId("report_security_issues").type(Type.INSERT_AFTER)
                                .stepId("setup-jdks")
                                .step(WorkflowStep.createStep(Map.of("id", "new-step", "name", "New Step"))).build())
                        .build()))
                .build()).getJob("report_security_issues");
        assertAll(() -> assertThat(job.getSteps(), hasSize(6)),
                () -> assertThat(job.getStep("new-step").getName(), equalTo("New Step")));
    }

    // [utest->dsn~customize-build-process.dependency-update~0]
    @Test
    void dependencyUpdateBuildCustomized() {
        final Job job = dependencyUpdateBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("dependencies_update.yml")
                        .addStep(StepCustomization.builder().jobId("update_dependencies").type(Type.INSERT_AFTER)
                                .stepId("setup-jdks")
                                .step(WorkflowStep.createStep(Map.of("id", "new-step", "name", "New Step"))).build())
                        .build()))
                .build()).getJob("update_dependencies");
        assertAll(() -> assertThat(job.getSteps(), hasSize(14)),
                () -> assertThat(job.getStep("new-step").getName(), equalTo("New Step")));
    }

    @Test
    void ciBuildMatrixBuildAllStepsHaveId() {
        final Job job = ciBuildContent(BuildOptions.builder().exasolDbVersions(List.of("v1"))).getJob("matrix-build");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(9)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void ciBuildNonMatrixAllStepsHaveId() {
        final Job job = ciBuildContent(BuildOptions.builder()).getJob("build-and-test");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(10)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void releaseBuildAllStepsHaveId() {
        final Job job = releaseBuildContent(BuildOptions.builder().build()).getJob("release");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(13)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void dependencyCheckBuildAllStepsHaveId() {
        final Job job = dependencyCheckBuildContent(BuildOptions.builder().build()).getJob("report_security_issues");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(5)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void dependencyUpdateBuildAllStepsHaveId() {
        final Job job = dependencyUpdateBuildContent(BuildOptions.builder().build()).getJob("update_dependencies");
        assertThat(job.getSteps(), hasSize(greaterThanOrEqualTo(13)));
        job.getSteps().forEach(step -> assertThat(step.getId(), notNullValue()));
    }

    @Test
    void customizeSetupJavaStepInCiBuild() {
        final Map<String, Object> setupJavaStep = setupJavaStep("custom-version");
        final Job job = ciBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("ci-build.yml")
                        .addStep(StepCustomization.builder().jobId("build-and-test").type(Type.REPLACE)
                                .stepId("setup-java").step(WorkflowStep.createStep(setupJavaStep)).build())
                        .build())))
                .getJob("build-and-test");
        final String customJavaVersion = (String) job.getStep("setup-java").getWith().get("java-version");
        assertThat(customJavaVersion, equalTo("custom-version"));
    }

    @Test
    void customizeSetupJavaStepInReleaseBuild() {
        final Map<String, Object> setupJavaStep = setupJavaStep("custom-version");
        final Job job = releaseBuildContent(BuildOptions.builder()
                .workflows(List.of(CustomWorkflow.builder().workflowName("release.yml")
                        .addStep(StepCustomization.builder().jobId("release").type(Type.REPLACE).stepId("setup-jdks")
                                .step(WorkflowStep.createStep(setupJavaStep)).build())
                        .build()))
                .build()).getJob("release");
        final String customJavaVersion = (String) job.getStep("setup-java").getWith().get("java-version");
        assertThat(customJavaVersion, equalTo("custom-version"));
    }

    @Test
    void brokenLinksCheckerWorkflowWithoutCustomization() {
        final GitHubWorkflow workflow = parse(
                testee(BuildOptions.builder().build()).createBrokenLinksCheckerWorkflow().getContent());
        assertThat(workflow.getOnTrigger().get("schedule"), equalTo(List.of(Map.of("cron", "0 5 * * 0"))));
    }

    @Test
    void brokenLinksCheckerWorkflowWithCustomization() {
        final Map<String, Object> setupJavaStep = setupJavaStep("custom-version");
        final GitHubWorkflow workflow = parse(
                testee(BuildOptions.builder()
                        .workflows(List.of(CustomWorkflow.builder().workflowName("broken_links_checker.yml")
                                .addStep(StepCustomization.builder().jobId("linkChecker").stepId("checkout")
                                        .type(Type.INSERT_AFTER).step(WorkflowStep.createStep(setupJavaStep)).build())
                                .build()))
                        .build()).createBrokenLinksCheckerWorkflow().getContent());
        final String customJavaVersion = (String) workflow.getJob("linkChecker").getStep("setup-java").getWith()
                .get("java-version");
        assertThat(customJavaVersion, equalTo("custom-version"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void projectKeeperVerifyWorkflowWithoutCustomization(final boolean hasNpmModule) {
        final Job job = parse(
                testee(BuildOptions.builder().build()).createProjectKeeperVerifyWorkflow(hasNpmModule).getContent())
                .getJob("project-keeper-verify");
        assertAll(
                () -> assertThat(job.getStep("setup-node").getIfCondition(),
                        equalTo("${{ %s }}".formatted(hasNpmModule))),
                () -> assertThat(job.getStep("project-keeper-verify").getRunCommand(),
                        equalTo("./.github/workflows/project-keeper.sh verify")));
    }

    @Test
    void projectKeeperVerifyWorkflowWithoutCustomization() {
        final Map<String, Object> setupJavaStep = setupJavaStep("custom-version");
        final Job job = parse(
                testee(BuildOptions.builder()
                        .workflows(List.of(CustomWorkflow.builder().workflowName("project-keeper-verify.yml")
                                .addStep(StepCustomization.builder().jobId("project-keeper-verify").stepId("setup-java")
                                        .type(Type.REPLACE).step(WorkflowStep.createStep(setupJavaStep)).build())
                                .build()))
                        .build())
                        .createProjectKeeperVerifyWorkflow(false).getContent())
                .getJob("project-keeper-verify");
        assertThat(job.getStep("setup-java").getWith().get("java-version"), equalTo("custom-version"));
    }

    private Map<String, Object> setupJavaStep(final String javaVersion) {
        final Map<String, Object> setupJavaStep = new HashMap<>();
        setupJavaStep.put("id", "setup-java");
        setupJavaStep.put("name", "New Java");
        setupJavaStep.put("uses", "actions/setup-java@v4");
        final Map<String, String> withElement = new HashMap<>();
        withElement.put("java-version", javaVersion);
        setupJavaStep.put("with", withElement);
        return setupJavaStep;
    }

    private GitHubWorkflow ciBuildContent(final BuildOptions.Builder optionsBuilder) {
        return ciBuildContent(optionsBuilder.build());
    }

    private GitHubWorkflow ciBuildContent(final BuildOptions options) {
        final FileTemplate template = testee(options).createCiBuildWorkflow();
        final String content = template.getContent();
        assertAll( //
                () -> assertThat(content, startsWith("# This file was generated by Project Keeper.\n")),
                () -> assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/ci-build.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return parse(content);
    }

    private GitHubWorkflow releaseBuildContent(final BuildOptions options, final AnalyzedMavenSource... sources) {
        final FileTemplate template = testee(options).createReleaseWorkflow(asList(sources));
        final String content = template.getContent();
        assertAll( //
                () -> assertThat(content, startsWith("# This file was generated by Project Keeper.\n")),
                () -> assertThat(template.getPathInProject(), equalTo(Path.of(".github/workflows/release.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return parse(content);
    }

    private GitHubWorkflow dependencyUpdateBuildContent(final BuildOptions options) {
        final FileTemplate template = testee(options).createDependenciesUpdateWorkflow();
        final String content = template.getContent();
        assertAll( //
                () -> assertThat(content, startsWith("# This file was generated by Project Keeper.\n")),
                () -> assertThat(template.getPathInProject(),
                        equalTo(Path.of(".github/workflows/dependencies_update.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return parse(content);
    }

    private GitHubWorkflow dependencyCheckBuildContent(final BuildOptions options) {
        final FileTemplate template = testee(options).createDependenciesCheckWorkflow();
        final String content = template.getContent();
        assertAll( //
                () -> assertThat(content, startsWith("# This file was generated by Project Keeper.\n")),
                () -> assertThat(template.getPathInProject(),
                        equalTo(Path.of(".github/workflows/dependencies_check.yml"))),
                () -> assertThat(template.getValidation(), equalTo(Validation.REQUIRE_EXACT)),
                () -> validateYamlSyntax(content));
        return parse(content);
    }

    private void validateYamlSyntax(final String content) {
        final Yaml yaml = new Yaml();
        assertDoesNotThrow(() -> yaml.load(content));
    }

    private CiBuildWorkflowGenerator testee(final BuildOptions options) {
        return new CiBuildWorkflowGenerator(options, List.of("11", "17"), () -> "21");
    }

    private GitHubWorkflow parse(final String yaml) {
        return GitHubWorkflowIO.create().loadWorkflow(yaml);
    }
}
