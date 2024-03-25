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

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.shared.config.*;

// [utest->dsn~modules~1]
class ProjectKeeperConfigReaderTest {
    private final ProjectKeeperConfigReader reader = new ProjectKeeperConfigReader();
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
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
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
                build:
                  runnerOs: custom-runner-os
                  freeDiskSpace: true
                  exasolDbVersions:
                    - v1
                    - v2
                linkReplacements:
                  - "http://wrong-url.com|my-dependency.de"
                """);
        final ProjectKeeperConfig config = this.reader.readConfig(this.tempDir);
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
                () -> assertThat(config.getExcludes(), containsInAnyOrder(
                        "\\QE-PK-CORE-17: Missing required file: '.github/workflows/broken_links_checker.yml'.\\E",
                        "E-PK-CORE-18: .*")),
                () -> assertThat(config.getLinkReplacements(),
                        Matchers.contains("http://wrong-url.com|my-dependency.de"))//
        );
    }

    @Test
    void readDefaults() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
                sources:
                  - type: maven
                    path: my-sub-project/pom.xml
                """);
        final ProjectKeeperConfig config = this.reader.readConfig(this.tempDir);
        final Source source = config.getSources().get(0);
        assertAll(//
                () -> assertThat(source.getType(), equalTo(MAVEN)),
                () -> assertThat(source.isAdvertised(), equalTo(true)),
                () -> assertThat(source.getPath(), equalTo(this.tempDir.resolve("my-sub-project/pom.xml"))),
                () -> assertThat(source.getModules(), Matchers.containsInAnyOrder(DEFAULT)),
                () -> assertThat(source.getParentPom(), nullValue()),
                () -> assertThat(config.getExcludes(), equalTo(Collections.emptyList())),
                () -> assertThat(config.getCiBuildConfig().getRunnerOs(), equalTo("ubuntu-latest")),
                () -> assertThat(config.getCiBuildConfig().shouldFreeDiskSpace(), equalTo(false)),
                () -> assertThat(config.getCiBuildConfig().getExasolDbVersions(), empty()),
                () -> assertThat(config.getLinkReplacements(), equalTo(Collections.emptyList()))//
        );
    }

    @Test
    void invalidSubprojectPath() throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
                sources:
                  - type: maven
                    path: unknown-project/pom.xml
                    modules:
                      - maven_central
                linkReplacements:
                  - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(),
                startsWith("E-PK-CORE-83: Invalid .project-keeper.yml. The specified path "));
    }

    @ParameterizedTest
    @ValueSource(strings = { "excludes:\n - 1: 3", "excludes:\n - 2", "excludes:\n - regex: 2" })
    void testInvalidExcludes(final String invalidExcludes) throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), invalidExcludes);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-87: Invalid .project-keeper.yml. Invalid value "));
    }

    @Test
    void incompleteConfig() throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
                    sources:
                      - type: maven
                    linkReplacements:
                      - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-86: Invalid .project-keeper.yml. Missing required property 'sources/path'."));
    }

    @Test
    void unknownType() throws IOException {
        Files.createDirectory(this.tempDir.resolve("my-sub-project"));
        Files.writeString(this.tempDir.resolve("my-sub-project/pom.xml"), "");
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
                    sources:
                      - type: unknown
                        path: my-sub-project/pom.xml
                        modules:
                          - maven_central
                    linkReplacements:
                      - "http://wrong-url.com|my-dependency.de"
                """);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(),
                equalTo("E-PK-CORE-84: Invalid .project-keeper.yml. Unsupported source type 'unknown'."
                        + " Please use one of the supported types: maven, golang, npm."));
    }

    @Test
    void readFixedVersion() throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), //
                "version: \"1.2.3\"");
        final ProjectKeeperConfig config = this.reader.readConfig(this.tempDir);
        assertThat(config.getVersionConfig(), equalTo(new FixedVersion("1.2.3")));
    }

    @Test
    void readVersionFromSource() throws IOException {
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), """
                version:
                  fromSource: "./pom.xml"
                """);
        final ProjectKeeperConfig config = this.reader.readConfig(this.tempDir);
        assertThat(config.getVersionConfig(), equalTo(new VersionFromSource(this.tempDir.resolve("./pom.xml"))));
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
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), "{ -");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(), startsWith("E-PK-CORE-85: Invalid .project-keeper.yml."));
    }

    @Test
    void notInProjectRoot() throws IOException {
        Files.delete(this.tempDir.resolve(".git"));
        Files.writeString(this.tempDir.resolve(".project-keeper.yml"), "");
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.reader.readConfig(this.tempDir));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-90: Could not find .git directory in project-root '"
                + this.tempDir
                + "'. Known mitigations:\n* Run 'git init'.\n* Make sure that you run project-keeper only in the root directory of the git-repository. If you have multiple projects in that directory, define them in the '.project-keeper.yml'."));
    }
}
