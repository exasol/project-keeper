package com.exasol.projectkeeper;

import static com.exasol.projectkeeper.ProjectKeeperModule.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.it.VerificationException;
import org.apache.maven.it.Verifier;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.validators.ProjectKeeperPluginDeclaration;
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
    void testVerify() throws IOException {
        writePomWithAllProjectKeeperPlugins();
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-6")),
                () -> assertThat(output,
                        containsString("E-PK-17: Missing required: '.settings" + File.separator
                                + "org.eclipse.jdt.core.prefs'")),
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin.")),
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.sonatype.ossindex.maven:ossindex-maven-plugin.")),
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-enforcer-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin com.exasol:artifact-reference-checker-maven-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-surefire-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-assembly-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.jacoco:jacoco-maven-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-failsafe-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-deploy-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-gpg-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-source-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-javadoc-plugin.")), //
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-jar-plugin.")), //
                () -> assertThat(output,
                        containsString(
                                "E-PK-15: Missing maven plugin org.apache.maven.plugins:maven-dependency-plugin.")), //
                () -> assertThat(output, containsString("E-PK-29: Missing dependency 'org.jacoco:org.jacoco.agent'.")), //
                () -> assertThat(output, containsString(
                        "E-PK-72: Missing required property '/project/properties/project.build.sourceEncoding' in pom.xml.")), //
                () -> assertThat(output, containsString("E-PK-29: Missing dependency 'org.projectlombok:lombok'.")), //
                () -> assertThat(output, containsString(
                        "E-PK-15: Missing maven plugin org.sonatype.plugins:nexus-staging-maven-plugin.")) //
        );
    }

    private void writePomWithAllProjectKeeperPlugins() throws IOException {
        final var pom = new TestMavenModel();
        pom.addProjectKeeperPlugin(new ProjectKeeperPluginDeclaration(CURRENT_VERSION).withEnabledModules(MAVEN_CENTRAL,
                INTEGRATION_TESTS, JAR_ARTIFACT, UDF_COVERAGE, LOMBOK));
        pom.writeAsPomToProject(this.projectDir);
    }

    @Test
    // [itest->dsn~excluding-files~1]
    void testVerifyWithExcludedFile() throws IOException {
        final var pom = new TestMavenModel();
        pom.addProjectKeeperPlugin(new ProjectKeeperPluginDeclaration(CURRENT_VERSION)
                .withEnabledModules(INTEGRATION_TESTS).withExcludedFiles("**/logging.properties"));
        pom.writeAsPomToProject(this.projectDir);
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertThat(output, not(containsString("logging.properties")));
    }

    @Test
    // [itest->dsn~deleted-files-validator~1]
    void testVerifyWithAFileThatMustNotExist() throws VerificationException, IOException {
        writePomWithAllProjectKeeperPlugins();
        final Path fileThatMustNotExist = this.projectDir.resolve(".github/workflows/maven.yml");
        fileThatMustNotExist.toFile().getParentFile().mkdirs();
        Files.writeString(fileThatMustNotExist, "some content");
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertThat(output, containsString("E-PK-26: '.github" + File.separator + "workflows" + File.separator
                + "maven.yml' exists but must not exist."));
    }

    @Test
    // [itest->dsn~exclduding-mvn-plugins~1]
    void testExcludedPlugin() throws IOException, VerificationException {
        final var pom = new TestMavenModel();
        pom.addProjectKeeperPlugin(new ProjectKeeperPluginDeclaration(CURRENT_VERSION)
                .withEnabledModules(MAVEN_CENTRAL, INTEGRATION_TESTS, JAR_ARTIFACT, UDF_COVERAGE)
                .withExcludedPlugins("com.exasol:error-code-crawler-maven-plugin"));
        pom.writeAsPomToProject(this.projectDir);
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertThat(output,
                not(containsString("E-PK-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")));
    }

    @Test
    void testOnlyPomInvalid() throws IOException, VerificationException {
        writePomWithAllProjectKeeperPlugins();
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        writePomWithAllProjectKeeperPlugins();// override pom
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-6")),
                () -> assertThat(output,
                        not(containsString("E-PK-17: Missing required: .settings" + File.separator
                                + "org.eclipse.jdt.core.prefs"))),
                () -> assertThat(output,
                        containsString("E-PK-15: Missing maven plugin org.codehaus.mojo:versions-maven-plugin."))//
        );
    }

    @Test
    // [itest->dsn~mvn-fix-goal~1]
    // [itest->dsn~license-file-validator~1]
    void testFix() throws VerificationException, IOException {
        writePomWithAllProjectKeeperPlugins();
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        verifier.verifyErrorFreeLog();
        assertThat(this.projectDir.resolve("LICENSE").toFile(), anExistingFile());
    }

    @Test
    void testValidAfterFix() throws VerificationException, IOException {
        writePomWithAllProjectKeeperPlugins();
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        assertDoesNotThrow(() -> verifier.executeGoal("project-keeper:verify"));
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    // [itest->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
    void testChangesFileGeneration(final boolean released) throws IOException, GitAPIException, VerificationException {
        setupDemoProjectWithDependencyChange(released);
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
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
    void testChangesFileGeneration() throws IOException, GitAPIException, VerificationException {
        setupDemoProjectWithDependencyChange(true);
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        final String generatedChangelog = Files.readString(this.projectDir.resolve("doc/changes/changelog.md"));
        assertThat(generatedChangelog, containsString("[0.2.0](changes_0.2.0.md)"));
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

    private void commitAndMakeTag(final Git git, final String releaseTag) throws GitAPIException {
        git.add().addFilepattern("pom.xml").call();
        git.commit().setMessage("commit for release " + releaseTag).call();
        git.tag().setName(releaseTag).call();
    }

    @Test
    void testJacocoAgentIsExtracted() throws VerificationException, IOException {
        writePomWithAllProjectKeeperPlugins();
        final Verifier verifier = getVerifier();
        verifier.executeGoal("project-keeper:fix");
        verifier.executeGoal("package");
        assertThat(this.projectDir.resolve(Path.of("target", "jacoco-agent", "org.jacoco.agent-runtime.jar")).toFile(),
                anExistingFile());
    }

    @Test
    // [itest->dsn~readme-validator~1]
    void testVerifyReadme() throws IOException {
        writePomWithAllProjectKeeperPlugins();
        Files.writeString(this.projectDir.resolve("README.md"), "");
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-61")), //
                () -> assertThat(output, containsString("E-PK-62"))//
        );
    }

    @Test
    void testExcludedReadme() throws IOException {
        final var pom = new TestMavenModel();
        pom.addProjectKeeperPlugin(new ProjectKeeperPluginDeclaration(CURRENT_VERSION)
                .withEnabledModules(MAVEN_CENTRAL, INTEGRATION_TESTS, JAR_ARTIFACT, UDF_COVERAGE)
                .withExcludedFiles("README.md"));
        pom.writeAsPomToProject(this.projectDir);
        final Verifier verifier = getVerifier();
        final VerificationException verificationException = assertThrows(VerificationException.class,
                () -> verifier.executeGoal("project-keeper:verify"));
        final String output = verificationException.getMessage();
        assertAll(//
                () -> assertThat(output, not(containsString("README.md"))), //
                () -> assertThat(output, not(containsString("README.md")))//
        );
    }
}
