package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.exasol.projectkeeper.test.MavenProjectFixture;
import com.exasol.projectkeeper.test.TestMavenModel;

/**
 * This integration test tests the maven plugin in a safe environment. Since we don't want to install the plugin to the
 * user's maven repository, it creates a temporary maven home, and installs the plugin there. Then the test creates a
 * temporary project, runs the plugin on that project and checks the output.
 */
@Tag("integration")
class ProjectKeeperIT extends ProjectKeeperAbstractMavenIT {

    private MavenProjectFixture fixture;

    @BeforeEach
    void setup() {
        this.fixture = new MavenProjectFixture(this.projectDir);
        this.fixture.gitInit();
    }

    @Test
    // [itest->dsn~pom-file-validator~1]
    void testVerifyPhase1() throws IOException {
        Files.writeString(this.projectDir.resolve("LICENSE"), "My License\n");
        this.fixture.writeDefaultPom();
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output,
                        containsString("E-PK-CORE-17: Missing required file: 'pk_generated_parent.pom'")),
                () -> assertThat(output, containsString(
                        "E-PK-CORE-105: Invalid pom file 'pom.xml': Missing required property finalName property in maven-assembly-plugin.")));
    }

    @Test
    // [itest->dsn~required-files-validator~1]
    // [itest->dsn~gitignore-validator~1]
    void testVerifyPhase2() throws IOException {
        writeDefaultPom();
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        Files.delete(this.projectDir.resolve(".settings/org.eclipse.jdt.core.prefs"));
        Files.delete(this.projectDir.resolve(".gitignore"));
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output,
                        containsString("E-PK-CORE-17: Missing required file: '.settings/org.eclipse.jdt.core.prefs'")),
                () -> assertThat(output, containsString("E-PK-CORE-56: Could not find required file '.gitignore'.")) //
        );
    }

    private void writeDefaultPom() throws IOException {
        final var pom = new TestMavenModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.projectDir);
    }

    @Test
    // [itest->dsn~excluding~1]
    void testVerifyWithExcludedFile() throws IOException {
        writeDefaultPom();
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder()
                .excludes(List.of("E-PK-CORE-17: Missing required file: 'src/test/resources/logging.properties'"
                        .replace('/', File.separatorChar))));
        final String output = assertInvalidAndGetOutput();
        assertThat(output, not(containsString("logging.properties")));
    }

    @Test
    // [itest->dsn~deleted-files-validator~1]
    void testVerifyWithAFileThatMustNotExist() throws IOException {
        writeDefaultPom();
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
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
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder().excludes(List
                .of("E-PK-CORE-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin. (in pom.xml)")));
        final String output = assertInvalidAndGetOutput();
        assertThat(output,
                not(containsString("E-PK-CORE-15: Missing maven plugin com.exasol:error-code-crawler-maven-plugin.")));
    }

    @Test
    // [itest->dsn~mvn-fix-goal~1]
    // [itest->dsn~license-file-validator~1]
    void testFix() throws IOException {
        final var pom = new TestMavenModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.projectDir);
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        final ToStringLogger logger = new ToStringLogger();
        final boolean success = getProjectKeeper(logger).fix();
        assertThat(success, equalTo(true));
        assertThat(this.projectDir.resolve("LICENSE").toFile(), anExistingFile());
    }

    @Test
    void testValidAfterFix() throws IOException {
        final var pom = new TestMavenModel();
        pom.configureAssemblyPluginFinalName();
        pom.writeAsPomToProject(this.projectDir);
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        assertVerifySucceeds();
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    // [itest->dsn~dependency-section-in-changes_x.x.x.md-file-validator~1]
    void testChangesFileGeneration(final boolean released) throws IOException, GitAPIException {
        setupDemoProjectWithDependencyChange(released);
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        final String generatedChangesFile = Files.readString(this.projectDir.resolve("doc/changes/changes_0.2.0.md"));
        assertAll(//
                () -> assertThat(generatedChangesFile, startsWith("# My Test Project 0.2.0, released")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `com.exasol:error-reporting-java:0.1.0` to `0.2.0`")),
                () -> assertThat(generatedChangesFile,
                        containsString("* Updated `org.apache.maven.plugins:maven-surefire-plugin:2.12.4` to")));
    }

    @Test
    // [itest->dsn~verify-changelog-file~1]
    void testChangelogFileGeneration() throws IOException, GitAPIException {
        setupDemoProjectWithDependencyChange(true);
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        final String generatedChangelog = Files.readString(this.projectDir.resolve("doc/changes/changelog.md"));
        assertThat(generatedChangelog, containsString("[0.2.0](changes_0.2.0.md)"));
    }

    @Test
    void testChangesFileGenerationWithNoPomInPrevVersion() throws IOException, GitAPIException {
        setupDemoProjectWithPomAddedInThisVersion();
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        final String generatedChangesFile = Files.readString(this.projectDir.resolve("doc/changes/changes_0.2.0.md"));
        assertThat(generatedChangesFile, containsString("* Added `com.exasol:error-reporting-java:0.2.0`"));
    }

    private void setupDemoProjectWithDependencyChange(final boolean released) throws IOException, GitAPIException {
        try (final Git git = Git.open(this.projectDir.toFile())) {
            this.fixture.writePomWithOneDependency("0.1.0", "0.1.0");
            commitAndMakeTag(git, "0.1.0");
            this.fixture.writePomWithOneDependency("0.2.0", "0.2.0");
            if (released) {
                commitAndMakeTag(git, "0.2.0");
            }
        }
    }

    private void setupDemoProjectWithPomAddedInThisVersion() throws IOException, GitAPIException {
        try (final Git git = Git.open(this.projectDir.toFile())) {
            Files.writeString(this.projectDir.resolve("a-file.txt"), "some content");
            commitAndMakeTag(git, "0.1.0");
            this.fixture.writePomWithOneDependency("0.2.0", "0.2.0");
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
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        runFix();
        Files.writeString(this.projectDir.resolve("README.md"), "");
        this.fixture.writeConfig(this.fixture.getConfigWithAllModulesBuilder());
        final String output = assertInvalidAndGetOutput();
        assertAll(//
                () -> assertThat(output, containsString("E-PK-CORE-61")), //
                () -> assertThat(output, containsString("E-PK-CORE-62"))//
        );
    }
}
