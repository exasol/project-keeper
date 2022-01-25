package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.ProjectKeeperModule.values;
import static com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType.MAVEN;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.apache.maven.it.VerificationException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.validators.TestMavenModel;

/**
 * This integration test tests the maven plugin in a safe environment. Since we don't want to install the plugin to the
 * user's maven repository, it creates a temporary maven home, and installs the plugin there. Then the test creates a
 * temporary project, runs the plugin on that project and checks the output.
 */
@Tag("integration")
class ProjectKeeperIT extends ProjectKeeperAbstractIT {

    @Test
    // [itest->dsn~mvn-plugin-validator~1]
    // [itest->dsn~mvn-dependency-validator~1]
    // [itest->dsn~mvn-verify-goal~1]
    // [itest->dsn~required-files-validator~1]
    // [itest->dsn~gitignore-validator~1]
    void testVerify() throws IOException {
        writeDefaultPom();
        writeConfig(createConfigWithAllModules());
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-CORE-6")),
                () -> assertThat(output,
                        containsString("E-PK-CORE-17: Missing required: '.settings" + File.separator
                                + "org.eclipse.jdt.core.prefs'")),
                () -> assertThat(output,
                        containsString("E-PK-CORE-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin.")),
                () -> assertThat(output, containsString(
                        "E-PK-CORE-15: Missing maven plugin org.sonatype.ossindex.maven:ossindex-maven-plugin.")),
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-enforcer-plugin.")), //
                () -> assertThat(output, containsString(
                        "E-PK-CORE-15: Missing maven plugin com.exasol:artifact-reference-checker-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-surefire-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-assembly-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-CORE-15: Missing maven plugin org.jacoco:jacoco-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-failsafe-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-deploy-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-gpg-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-source-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-javadoc-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-jar-plugin.")), //
                () -> assertThat(output, containsString(
                        "E-PK-CORE-15: Missing maven plugin org.apache.maven.plugins:maven-dependency-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-CORE-29: Missing dependency 'org.jacoco:org.jacoco.agent'.")), //
                () -> assertThat(output, containsString(
                        "E-PK-CORE-72: Missing required property '/project/properties/project.build.sourceEncoding' in pom.xml.")), //
                () -> assertThat(output,
                        containsString("E-PK-CORE-29: Missing dependency 'org.projectlombok:lombok'.")), //
                () -> assertThat(output, containsString("E-PK-CORE-56: Could not find required file '.gitignore'.")), //
                () -> assertThat(output, containsString(
                        "E-PK-CORE-15: Missing maven plugin org.sonatype.plugins:nexus-staging-maven-plugin.")) //
        );
    }

    private ProjectKeeperConfig createConfigWithAllModules() {
        return getConfigWithAllModulesBuilder().build();
    }

    private ProjectKeeperConfig.ProjectKeeperConfigBuilder getConfigWithAllModulesBuilder() {
        return ProjectKeeperConfig.builder().sources(List.of(ProjectKeeperConfig.Source.builder()
                .modules(Set.of(values())).type(MAVEN).path(Path.of("pom.xml")).build()));
    }

    private void writeDefaultPom() throws IOException {
        final var pom = new TestMavenModel();
        pom.writeAsPomToProject(this.projectDir);
    }

    // todo test with config file

    @Test
    // [itest->dsn~excluding~1]
    void testVerifyWithExcludedFile() throws IOException {
        writeDefaultPom();
        writeConfig(getConfigWithAllModulesBuilder()
                .excludes(List.of("E-PK-CORE-17: Missing required: 'src/test/resources/logging.properties'")).build());
        final String output = assertInvalidAndGetOutput();
        assertThat(output, not(containsString("logging.properties")));
    }

    @Test
    // [itest->dsn~deleted-files-validator~1]
    void testVerifyWithAFileThatMustNotExist() throws IOException {
        writeDefaultPom();
        writeConfig(createConfigWithAllModules());
        final Path fileThatMustNotExist = this.projectDir.resolve(".github/workflows/maven.yml");
        Files.createDirectories(fileThatMustNotExist.getParent());
        Files.writeString(fileThatMustNotExist, "some content");
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-26: '.github" + File.separator + "workflows" + File.separator
                + "maven.yml' exists but must not exist."));
    }

    @Test
    // [itest->dsn~excluding~1]
    void testExcludedPlugin() throws IOException {
        writeDefaultPom();
        writeConfig(getConfigWithAllModulesBuilder()
                .excludes(List.of("E-PK-CORE-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin."))
                .build());
        final String output = assertInvalidAndGetOutput();
        assertThat(output,
                not(containsString("E-PK-CORE-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")));
    }

    @Test
    void testOnlyPomInvalid() throws IOException {
        writeDefaultPom();
        writeConfig(createConfigWithAllModules());
        runFix();
        writeDefaultPom();// override pom
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-CORE-6")),
                () -> assertThat(output,
                        not(containsString("E-PK-CORE-17: Missing required: .settings" + File.separator
                                + "org.eclipse.jdt.core.prefs"))),
                () -> assertThat(output,
                        containsString("E-PK-CORE-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))//
        );
    }

    @Test
    // [itest->dsn~mvn-fix-goal~1]
    // [itest->dsn~license-file-validator~1]
    void testFix() throws IOException {
        writeDefaultPom();
        writeConfig(createConfigWithAllModules());
        final boolean success = getProjectKeeper(mock(Logger.class)).fix();
        assertThat(success, equalTo(true));
        assertThat(this.projectDir.resolve("LICENSE").toFile(), anExistingFile());
    }

    @Test
    void testValidAfterFix() throws IOException {
        writeDefaultPom();
        writeConfig(createConfigWithAllModules());
        runFix();
        assertVerifySucceeds();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    // [itest->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
    void testChangesFileGeneration(final boolean released) throws IOException, GitAPIException {
        setupDemoProjectWithDependencyChange(released);
        writeConfig(createConfigWithAllModules());
        runFix();
        final String generatedChangesFile = Files.readString(this.projectDir.resolve("doc/changes/changes_0.2.0.md"));
        assertAll(//
                () -> assertThat(generatedChangesFile, startsWith("# my-test-project 0.2.0, released")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `com.exasol:error-reporting-java:0.1.0` to `0.2.0`")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `org.apache.maven.plugins:maven-surefire-plugin:2.12.4` to")));
    }

    @Test
    // [itest->dsn~verify-changelog-file~1]
    void testChangelogFileGeneration() throws IOException, GitAPIException {
        setupDemoProjectWithDependencyChange(true);
        writeConfig(createConfigWithAllModules());
        runFix();
        final String generatedChangelog = Files.readString(this.projectDir.resolve("doc/changes/changelog.md"));
        assertThat(generatedChangelog, containsString("[0.2.0](changes_0.2.0.md)"));
    }

    @Test
    void testChangesFileGenerationWithNoPomInPrevVersion() throws IOException, GitAPIException, VerificationException {
        setupDemoProjectWithPomAddedInThisVersion();
        writeConfig(createConfigWithAllModules());
        runFix();
        final String generatedChangesFile = Files.readString(this.projectDir.resolve("doc/changes/changes_0.2.0.md"));
        assertThat(generatedChangesFile, containsString("* Added `com.exasol:error-reporting-java:0.2.0`"));
    }

    private void setupDemoProjectWithDependencyChange(final boolean released) throws IOException, GitAPIException {
        try (final Git git = Git.open(this.projectDir.toFile())) {
            writePomWithOneDependency("0.1.0", "0.1.0");
            commitAndMakeTag(git, "0.1.0");
            writePomWithOneDependency("0.2.0", "0.2.0");
            if (released) {
                commitAndMakeTag(git, "0.2.0");
            }
        }
    }

    private void setupDemoProjectWithPomAddedInThisVersion() throws IOException, GitAPIException {
        try (final Git git = Git.open(this.projectDir.toFile())) {
            Files.writeString(this.projectDir.resolve("a-file.txt"), "some content");
            commitAndMakeTag(git, "0.1.0");
            writePomWithOneDependency("0.2.0", "0.2.0");
        }
    }

    private void commitAndMakeTag(final Git git, final String releaseTag) throws GitAPIException {
        git.add().addFilepattern("pom.xml").call();
        git.commit().setMessage("commit for release " + releaseTag).call();
        git.tag().setName(releaseTag).call();
    }

    @Test
    // [itest->dsn~readme-validator~1]
    void testVerifyReadme() throws IOException {
        writeDefaultPom();
        Files.writeString(this.projectDir.resolve("README.md"), "");
        writeConfig(createConfigWithAllModules());
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-CORE-61")), //
                () -> assertThat(output, containsString("E-PK-CORE-62"))//
        );
    }
}
