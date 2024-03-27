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
import java.util.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.shared.config.*;

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

    @Test
    void invalidSubprojectPath() throws IOException {
        writeProjectKeeperConfig("""
                sources:
                  - type: maven
                    path: unknown-project/pom.xml
                    modules:
                      - maven_central
                linkReplacements:
                  - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-83: Invalid .project-keeper.yml. The specified path "));
    }

    @ParameterizedTest
    @ValueSource(strings = { "excludes:\n - 1: 3", "excludes:\n - 2", "excludes:\n - regex: 2" })
    void testInvalidExcludes(final String invalidExcludes) throws IOException {
        writeProjectKeeperConfig(invalidExcludes);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-87: Invalid .project-keeper.yml. Invalid value "));
    }

    @Test
    void incompleteConfig() throws IOException {
        writeProjectKeeperConfig("""
                    sources:
                      - type: maven
                    linkReplacements:
                      - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-86: Invalid .project-keeper.yml. Missing required property 'sources/path'."));
    }

    @Test
    void unknownType() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        writeProjectKeeperConfig("""
                    sources:
                      - type: unknown
                        path: my-sub-project/pom.xml
                        modules:
                          - maven_central
                    linkReplacements:
                      - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-84: Invalid .project-keeper.yml. Unsupported source type 'unknown'."
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
    void readNoCustomBuildConfiguration() throws IOException {
        writeProjectKeeperConfig("build:");
        final ProjectKeeperConfig config = readConfig();
        final BuildOptions buildConfig = config.getCiBuildConfig();
        assertAll(() -> assertThat(buildConfig.getSetupSteps(), empty()),
                () -> assertThat(buildConfig.getBuildStep().isEmpty(), is(true)),
                () -> assertThat(buildConfig.getCleanupSteps(), empty()));
    }

    @Test
    void readEmptyCustomBuildConfiguration() throws IOException {
        writeProjectKeeperConfig("""
                build:
                  setupSteps:
                  buildStep:
                  cleanupSteps:
                """);
        final ProjectKeeperConfig config = readConfig();
        final BuildOptions buildConfig = config.getCiBuildConfig();
        assertAll(() -> assertThat(buildConfig.getSetupSteps(), empty()),
                () -> assertThat(buildConfig.getBuildStep().isEmpty(), is(true)),
                () -> assertThat(buildConfig.getCleanupSteps(), empty()));
    }

    @Test
    void readCustomBuildStepMultiLine() throws IOException {
        writeProjectKeeperConfig("""
                build:
                  buildStep:
                    name: Build
                    run: |
                      echo 'build1'
                      echo 'build2'
                """);
        final ProjectKeeperConfig config = readConfig();
        final BuildOptions buildConfig = config.getCiBuildConfig();
        assertThat(buildConfig.getBuildStep().get(),
                equalTo(WorkflowStep.createStep(Map.of("name", "Build", "run", "echo 'build1'\necho 'build2'\n"))));
    }

    @Test
    void readSingleCustomSetupStep() throws IOException {
        writeProjectKeeperConfig("""
                build:
                  setupSteps:
                    - name: Setup
                      run: echo 'setup'
                      env:
                        MY_ENV: 'my-value1'
                        SECRET: ${{ secrets.SECRET }}
                  buildStep:
                    name: Build
                    run: echo 'build'
                    env:
                      MY_ENV: 'my-value2'
                      SECRET: ${{ secrets.SECRET }}
                  cleanupSteps:
                    - name: Cleanup
                      run: echo 'cleanup'
                      env:
                        MY_ENV: 'my-value3'
                        SECRET: ${{ secrets.SECRET }}
                """);
        final ProjectKeeperConfig config = readConfig();
        final List<WorkflowStep> setupSteps = config.getCiBuildConfig().getSetupSteps();
        final Optional<WorkflowStep> buildStep = config.getCiBuildConfig().getBuildStep();
        final List<WorkflowStep> cleanupSteps = config.getCiBuildConfig().getCleanupSteps();
        assertAll(() -> assertThat("setupSteps", setupSteps, hasSize(1)),
                () -> assertThat("setupSteps", setupSteps.get(0),
                        equalTo(WorkflowStep.createStep(Map.of("name", "Setup", "run", "echo 'setup'", "env",
                                Map.of("MY_ENV", "my-value1", "SECRET", "${{ secrets.SECRET }}"))))),
                //
                () -> assertThat("buildStep", buildStep.isPresent(), is(true)),
                () -> assertThat("buildStep", buildStep.get(),
                        equalTo(WorkflowStep.createStep(Map.of("name", "Build", "run", "echo 'build'", "env",
                                Map.of("MY_ENV", "my-value2", "SECRET", "${{ secrets.SECRET }}"))))),
                //
                () -> assertThat("cleanupSteps", cleanupSteps, hasSize(1)),
                () -> assertThat("cleanupSteps", cleanupSteps.get(0),
                        equalTo(WorkflowStep.createStep(Map.of("name", "Cleanup", "run", "echo 'cleanup'", "env",
                                Map.of("MY_ENV", "my-value3", "SECRET", "${{ secrets.SECRET }}"))))) //
        );
    }

    @Test
    void configFileMissing() {
        final ProjectKeeperConfigReader reader = new ProjectKeeperConfigReader();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-89: Could not find '.project-keeper.yml'. Please create this configuration according to the user-guide https://github.com/exasol/project-keeper-maven-plugin."));
    }

    @Test
    void invalidYamlSyntax() throws IOException {
        writeProjectKeeperConfig("{ -");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-85: Invalid .project-keeper.yml."));
    }

    @Test
    void notInProjectRoot() throws IOException {
        Files.delete(this.tempDir.resolve(".git"));
        writeProjectKeeperConfig("");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, this::readConfig);
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-90: Could not find .git directory in project-root '"
                + this.tempDir
                + "'. Known mitigations:\n* Run 'git init'.\n* Make sure that you run project-keeper only in the root directory of the git-repository. If you have multiple projects in that directory, define them in the '.project-keeper.yml'."));
    }

    private void writeProjectKeeperConfig(final String content) throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), content);
    }

    private ProjectKeeperConfig readConfig() {
        return new ProjectKeeperConfigReader().readConfig(this.tempDir);
    }
}
