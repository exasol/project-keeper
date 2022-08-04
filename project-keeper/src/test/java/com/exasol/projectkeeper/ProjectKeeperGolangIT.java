package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.*;

import com.exasol.projectkeeper.test.GolangProjectFixture;

@Tag("integration")
class ProjectKeeperGolangIT extends ProjectKeeperAbstractIT {

    private GolangProjectFixture fixture;

    @BeforeEach
    void setup() {
        this.fixture = new GolangProjectFixture(this.projectDir);
        this.fixture.gitInit();
    }

    @Test
    void testVerifyPhase1() throws IOException {
        prepareProjectFiles();
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-56: Could not find required file 'LICENSE'."));
    }

    @Test
    void fixingSolvesAllIssues() throws IOException {
        prepareProjectFiles();
        runFix();
        assertVerifySucceeds();
        assertGeneratedFiles();
    }

    // [itest -> dsn~golang-project-version~1]
    // [itest -> dsn~golang-dependency-licenses~1]
    // [itest -> dsn~golang-changed-dependency~1]
    private void assertGeneratedFiles() throws IOException {
        final String dependencies = Files.readString(this.projectDir.resolve("dependencies.md"));
        final String changelog = Files
                .readString(this.projectDir.resolve("doc/changes/changes_" + this.fixture.getProjectVersion() + ".md"));
        assertThat(changelog, allOf(containsString("* Added `golang:1.17`"),
                containsString("* Added `github.com/exasol/exasol-driver-go:v0.4.3`"), //
                containsString("* Added `github.com/exasol/exasol-test-setup-abstraction-server/go-client:v0.2.2`")));
        assertThat(dependencies, allOf(containsString("github.com/exasol/exasol-driver-go | [MIT][0]"), //
                containsString("| github.com/exasol/exasol-test-setup-abstraction-server/go-client | [MIT][1]")));
    }

    @Test
    void testVerifyPhase2() throws IOException {
        prepareProjectFiles();
        runFix();
        Files.delete(this.projectDir.resolve(".gitignore"));
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-56: Could not find required file '.gitignore'."));
    }

    private void prepareProjectFiles() throws IOException {
        this.fixture.prepareProjectFiles(this.fixture.createDefaultConfig());
    }
}
