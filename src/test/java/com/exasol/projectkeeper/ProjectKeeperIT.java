package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Tag("integration")
@Testcontainers
class ProjectKeeperIT {
    private static final File PLUGIN = Path.of("target", "project-keeper-maven-plugin-0.3.0.jar").toFile();
    private static final File PLUGIN_POM = Path.of("pom.xml").toFile();
    private static final File TEST_PROJECT = Path.of("src", "test", "resources", "test_project").toFile();

    @Container
    public static GenericContainer mvnContainer = new GenericContainer("maven:3.6.3-openjdk-11")
            .withFileSystemBind(PLUGIN.getAbsolutePath(), "/project_keeper.jar", BindMode.READ_ONLY)
            .withFileSystemBind(PLUGIN_POM.getAbsolutePath(), "/plugin_pom.xml", BindMode.READ_ONLY)
            .withFileSystemBind(TEST_PROJECT.getAbsolutePath(), "/test_project", BindMode.READ_ONLY)
            .withCommand("tail", "-f", "/dev/null");

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        runWithCheck("mvn", "--batch-mode", "install:install-file", "-Dfile=/project_keeper.jar",
                "-DpomFile=/plugin_pom.xml", "--log-file", "/dev/stdout");
    }

    private static void runWithCheck(final String... command) throws IOException, InterruptedException {
        final ExecResult result = mvnContainer.execInContainer(command);
        System.out.println(result.getStdout());
        System.out.println(result.getStderr());
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Command " + String.join(" ", command) + " failed");
        }
    }

    @AfterEach
    void after() throws IOException, InterruptedException {
        runWithCheck("rm", "-rf", "/tmp/test_project");
    }

    @Test
    void testVerify() throws IOException, InterruptedException {
        runWithCheck("cp", "-r", "/test_project", "/tmp/test_project");// copy to make it writeable
        final ExecResult result = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "package", "--log-file", "/dev/stdout", "--no-transfer-progress");
        assertAll(//
                () -> assertThat(result.getExitCode(), not(is(0))),
                () -> assertThat(result.getStdout(), containsString("E-PK-6")),
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-17: Missing required: .settings/org.eclipse.jdt.core.prefs")),
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin.")),
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin org.sonatype.ossindex.maven:ossindex-maven-plugin.")),
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-enforcer-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin com.exasol:artifact-reference-checker-maven-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-surefire-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-assembly-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-15: Missing maven plugin org.jacoco:jacoco-maven-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-failsafe-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-deploy-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-gpg-plugin.")), //
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-26: '.github/workflows/maven.yml' exists but must not exist.")), //
                () -> assertThat(result.getStdout(), containsString(
                        "E-PK-15: Missing maven plugin org.sonatype.plugins:nexus-staging-maven-plugin."))//
        );
    }

    @Test
    void testOnlyPomInvalid() throws IOException, InterruptedException {
        runWithCheck("cp", "-r", "/test_project", "/tmp/test_project");// copy to make it writeable
        mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f", "/tmp/test_project/pom.xml",
                "project-keeper:fix", "--log-file", "/dev/stdout", "--no-transfer-progress");
        runWithCheck("cp", "-rf", "/test_project/pom.xml", "/tmp/test_project/pom.xml");
        final ExecResult result = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "project-keeper:verify", "--log-file", "/dev/stdout",
                "--no-transfer-progress");
        assertAll(//
                () -> assertThat(result.getExitCode(), not(is(0))),
                () -> assertThat(result.getStdout(), containsString("E-PK-6")),
                () -> assertThat(result.getStdout(),
                        not(containsString("E-PK-17: Missing required: .settings/org.eclipse.jdt.core.prefs"))),
                () -> assertThat(result.getStdout(),
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))//
        );
    }

    @Test
    void testFix() throws IOException, InterruptedException {
        runWithCheck("cp", "-r", "/test_project", "/tmp/test_project");// copy to make it writeable
        final ExecResult fixResult = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "project-keeper:fix", "--log-file", "/dev/stdout",
                "--no-transfer-progress");
        final ExecResult result = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "project-keeper:verify", "--log-file", "/dev/stdout",
                "--no-transfer-progress");
        System.out.println(result.getStdout());
        System.out.println(result.getStderr());
        assertThat(result.getExitCode(), is(0));
    }

    @Test
    void testManualFixRequired() throws IOException, InterruptedException {
        runWithCheck("cp", "-r", "/test_project", "/tmp/test_project");// copy to make it writeable
        runWithCheck("rm", "-rf", "/tmp/test_project/doc/changes/changes_1.0.0.md");
        final ExecResult fixResult = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "project-keeper:fix", "--log-file", "/dev/stdout",
                "--no-transfer-progress");
        final ExecResult result = mvnContainer.execInContainer("mvn", "--batch-mode", "-e", "-f",
                "/tmp/test_project/pom.xml", "project-keeper:verify", "--log-file", "/dev/stdout",
                "--no-transfer-progress");
        assertThat(result.getStdout(), containsString(
                "E-PK-25: This projects structure does not conform with the template.\n" + "Please fix it manually."));
    }
}
