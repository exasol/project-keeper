package com.exasol.projectkeeper.config;

import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.DEFAULT;
import static com.exasol.projectkeeper.shared.config.ProjectKeeperModule.MAVEN_CENTRAL;
import static com.exasol.projectkeeper.shared.config.SourceType.MAVEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.workflow.*;

// [utest->dsn~modules~1]
class ProjectKeeperConfigReaderTest {
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectory(this.tempDir.resolve(".git"));
    }

    // [utest->dsn~excluding~1]
    @Test
    void read() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        writeProjectKeeperConfig("""
                excludes:
                 - "E-PK-CORE-17: Missing required file: '.github/workflows/broken_links_checker.yml'."
                 - regex: "E-PK-CORE-18: .*"
                sources:
                  - type: maven
                    path: my-sub-project/pom.xml
                    modules:
                      - maven_central
                    advertise: false
                    parentPom:
                      groupId: "com.example"
                      artifactId: "my-parent"
                      version: "1.2.3"
                      relativePath: "./my-parent.xml"
                    artifacts:
                      - "target/file1.jar"
                      - "target/file-${version}.jar"
                build:
                  runnerOs: custom-runner-os
                  freeDiskSpace: true
                  exasolDbVersions:
                    - v1
                    - v2
                linkReplacements:
                  - "http://wrong-url.com|my-dependency.de"
                """);
        final ProjectKeeperConfig config = readConfig();
        final Source source = config.getSources().get(0);
        assertAll(//
                () -> assertThat(config.getCiBuildConfig().getRunnerOs(), equalTo("custom-runner-os")),
                () -> assertThat(config.getCiBuildConfig().shouldFreeDiskSpace(), equalTo(true)),
                () -> assertThat(config.getCiBuildConfig().getExasolDbVersions(), contains("v1", "v2")),
                () -> assertThat(source.getType(), equalTo(MAVEN)),
                () -> assertThat(source.isAdvertised(), equalTo(false)),
                () -> assertThat(source.getPath(), equalTo(this.tempDir.resolve("my-sub-project/pom.xml"))),
                () -> assertThat(source.getModules(), Matchers.containsInAnyOrder(MAVEN_CENTRAL, DEFAULT)),
                () -> assertThat(source.getParentPom(),
                        equalTo(new ParentPomRef("com.example", "my-parent", "1.2.3", "./my-parent.xml"))),
                () -> assertThat(source.getReleaseArtifacts(),
                        contains(Path.of("target/file1.jar"), Path.of("target/file-${version}.jar"))),
                () -> assertThat(config.getExcludes(), containsInAnyOrder(
                        "\\QE-PK-CORE-17: Missing required file: '.github/workflows/broken_links_checker.yml'.\\E",
                        "E-PK-CORE-18: .*")),
                () -> assertThat(config.getLinkReplacements(),
                        Matchers.contains("http://wrong-url.com|my-dependency.de")) //
        );
    }

    @Test
    void readDefaults() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: my-sub-project/pom.xml
                """);
        final ProjectKeeperConfig config = readConfig();
        final Source source = config.getSources().get(0);
        assertAll(//
                () -> assertThat(source.getType(), equalTo(MAVEN)),
                () -> assertThat(source.isAdvertised(), equalTo(true)),
                () -> assertThat(source.getPath(), equalTo(this.tempDir.resolve("my-sub-project/pom.xml"))),
                () -> assertThat(source.getModules(), Matchers.containsInAnyOrder(DEFAULT)),
                () -> assertThat(source.getParentPom(), nullValue()),
                () -> assertThat(source.getReleaseArtifacts(), empty()),
                () -> assertThat(config.getExcludes(), equalTo(Collections.emptyList())),
                () -> assertThat(config.getCiBuildConfig().getRunnerOs(), equalTo("ubuntu-latest")),
                () -> assertThat(config.getCiBuildConfig().shouldFreeDiskSpace(), equalTo(false)),
                () -> assertThat(config.getCiBuildConfig().getExasolDbVersions(), empty()),
                () -> assertThat(config.getLinkReplacements(), equalTo(Collections.emptyList())) //
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "excludes:\n - 1: 3", "excludes:\n - 2", "excludes:\n - regex: 2" })
    void testInvalidExcludes(final String invalidExcludes) throws IOException {
        writeProjectKeeperConfig(invalidExcludes);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-87: Invalid file .project-keeper.yml. Invalid value "));
    }

    @Test
    void unknownType() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        writeProjectKeeperConfig("""
                    sources:
                      - type: unknown
                        path: my-sub-project/pom.xml
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-84: Invalid file .project-keeper.yml. Unsupported source type 'unknown'."
                        + " Please use one of the supported types: maven, golang, npm."));
    }

    @Test
    void readFixedVersion() throws IOException {
        writeProjectKeeperConfig("version: \"1.2.3\"");
        final ProjectKeeperConfig config = readConfig();
        assertThat(config.getVersionConfig(), equalTo(new FixedVersion("1.2.3")));
    }

    @Test
    void readVersionFromSource() throws IOException {
        writeProjectKeeperConfig("""
                version:
                  fromSource: "./pom.xml"
                """);
        final ProjectKeeperConfig config = readConfig();
        assertThat(config.getVersionConfig(), equalTo(new VersionFromSource(this.tempDir.resolve("./pom.xml"))));
    }

    @Test
    void readMissingWorkflows() throws IOException {
        writeProjectKeeperConfig("build:");
        assertThat(readConfig().getCiBuildConfig().getWorkflows(), empty());
    }

    @Test
    void readEmptyWorkflows() throws IOException {
        writeProjectKeeperConfig("""
                build:
                  workflows:
                """);
        assertThat(readConfig().getCiBuildConfig().getWorkflows(), empty());
    }

    static Stream<Arguments> invalidConfig() {
        return Stream.of(Arguments.of("missing config file", null, equalTo(
                "E-PK-CORE-89: Could not find file '.project-keeper.yml'. Please create this file according to the user-guide https://github.com/exasol/project-keeper-maven-plugin.")),
                Arguments.of("unsupported workflow name", """
                        build:
                          workflows:
                            - name: unsupported.yml
                        """, startsWith(
                        "E-PK-CORE-198: Unsupported workflow name 'unsupported.yml' found in file '.project-keeper.yml'. Please only use one of the supported workflows from [")),
                Arguments.of("missing source path", """
                            sources:
                              - type: maven
                            linkReplacements:
                              - "http://wrong-url.com|my-dependency.de"
                        """, equalTo(
                        "E-PK-CORE-86: Invalid file .project-keeper.yml. Required property 'sources/path' is missing.")),
                Arguments.of("invalid yaml syntax", "{ -",
                        startsWith("E-PK-CORE-85: Invalid file .project-keeper.yml.")),
                Arguments.of("invalid source path", """
                        sources:
                          - type: maven
                            path: unknown-project/pom.xml
                        """, startsWith("E-PK-CORE-83: Invalid file .project-keeper.yml. The specified path ")),
                Arguments.of("workflow: missing workflow name", """
                        build:
                          workflows:
                            - stepCustomizations:
                        """, equalTo(
                        "E-PK-CORE-199: Missing workflow name in file '.project-keeper.yml'. Add a workflow name to the workflow configuration.")),
                Arguments.of("workflow: missing step id", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - action: REPLACE
                                  content:
                                    id: new-step
                        """, equalTo(
                        "E-PK-CORE-201: Missing stepId in step customization of file '.project-keeper.yml'. Add stepId to the step customization.")),
                Arguments.of("workflow: empty step id", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - action: REPLACE
                                  stepId: ''
                                  content:
                                    id: new-step
                        """, equalTo(
                        "E-PK-CORE-201: Missing stepId in step customization of file '.project-keeper.yml'. Add stepId to the step customization.")),
                Arguments.of("workflow: missing action", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - stepId: step-id-to-replace
                                  content:
                                    id: new-step
                        """, equalTo(
                        "E-PK-CORE-200: Missing action in step customization of file '.project-keeper.yml'. Add action with one of values [INSERT_AFTER, REPLACE].")),
                Arguments.of("workflow: invalid action", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - action: INVALID
                                  stepId: step-id-to-replace
                                  content:
                                    id: new-step
                        """, startsWith("E-PK-CORE-85: Invalid file .project-keeper.yml. Path:")),
                Arguments.of("workflow: missing content", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - action: REPLACE
                                  stepId: step-id-to-replace
                        """, equalTo(
                        "E-PK-CORE-202: Missing content in step customization of file '.project-keeper.yml'. Add content to the step customization.")),
                Arguments.of("workflow: empty content", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                                - action: REPLACE
                                  stepId: step-id-to-replace
                                  content:
                        """, equalTo(
                        "E-PK-CORE-202: Missing content in step customization of file '.project-keeper.yml'. Add content to the step customization.")),
                Arguments.of("workflow: missing step customization list", """
                        build:
                          workflows:
                            - name: ci-build.yml
                        """, equalTo(
                        "E-PK-CORE-203: Missing customized steps for workflow 'ci-build.yml' in file '.project-keeper.yml'. Add at least one step or remove the workflow.")),
                Arguments.of("workflow: empty step customization list", """
                        build:
                          workflows:
                            - name: ci-build.yml
                              stepCustomizations:
                        """, equalTo(
                        "E-PK-CORE-203: Missing customized steps for workflow 'ci-build.yml' in file '.project-keeper.yml'. Add at least one step or remove the workflow.")));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidConfig")
    void testReadingFails(final String testName, final String content, final Matcher<String> expectedErrorMessage)
            throws IOException {
        if (content != null) {
            writeProjectKeeperConfig(content);
        }
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(testName, exception.getMessage(), expectedErrorMessage);
    }

    @Test
    void readWorkflowCustomization() throws IOException {
        writeProjectKeeperConfig("""
                build:
                  workflows:
                    - name: ci-build.yml
                      stepCustomizations:
                        - action: REPLACE
                          stepId: step-id-to-replace
                          content:
                            name: New Step
                            id: new-step
                            run: echo 'new step'
                            env:
                              ENV_VARIABLE: 'value'
                """);
        assertThat(readConfig().getCiBuildConfig().getWorkflows(), contains(CustomWorkflow.builder() //
                .workflowName("ci-build.yml") //
                .addStep(StepCustomization.builder() //
                        .type(StepCustomization.Type.REPLACE) //
                        .stepId("step-id-to-replace") //
                        .step(WorkflowStep.createStep(Map.of("name", "New Step", "id", "new-step", "run",
                                "echo 'new step'", "env", Map.of("ENV_VARIABLE", "value")))) //
                        .build()) //
                .build()));
    }

    @ParameterizedTest
    @CsvSource({ "REPLACE, REPLACE", "INSERT_AFTER, INSERT_AFTER" })
    void readWorkflowStepAction(final String action, final StepCustomization.Type expected) throws IOException {
        writeProjectKeeperConfig("""
                build:
                  workflows:
                    - name: ci-build.yml
                      stepCustomizations:
                        - action: ${action}
                          stepId: step-id-to-replace
                          content:
                            id: new-step
                """.replace("${action}", action));
        assertThat(readConfig().getCiBuildConfig().getWorkflows().get(0).getSteps().get(0).getType(),
                equalTo(expected));
    }

    @Test
    void notInProjectRoot() throws IOException {
        Files.delete(this.tempDir.resolve(".git"));
        writeProjectKeeperConfig("");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-90: Could not find .git directory in project-root '"
                + this.tempDir
                + "'. Known mitigations:\n* Run 'git init'.\n* Make sure that you run project-keeper only in the root directory of the git-repository. If you have multiple projects in that directory, define them in file '.project-keeper.yml'."));
    }

    private void writeProjectKeeperConfig(final String content) throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), content);
    }

    private ProjectKeeperConfig readConfig() {
        return new ProjectKeeperConfigReader().readConfig(this.tempDir);
    }
}
