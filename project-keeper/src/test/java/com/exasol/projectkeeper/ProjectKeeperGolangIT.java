package com.exasol.projectkeeper;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.*;

@Tag("integration")
class ProjectKeeperGolangIT extends ProjectKeeperAbstractIT {

    private GolangProjectFixture fixture;

    @BeforeEach
    void setup() {
        this.fixture = new GolangProjectFixture(this.projectDir);
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

    private void assertGeneratedFiles() throws IOException {
        final String dependencies = Files.readString(this.projectDir.resolve("dependencies.md"));
        final String changelog = Files
                .readString(this.projectDir.resolve("doc/changes/changes_" + this.fixture.getProjectVersion() + ".md"));
        assertThat(changelog, allOf(containsString("* Added `golang:1.17`"),
                containsString("* Added `github.com/exasol/exasol-driver-go:v0.3.1`")));
        assertThat(dependencies, containsString("github.com/exasol/exasol-driver-go | [MIT][0] "));
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
        this.fixture.writeConfig(this.fixture.createDefaultConfig());
        this.fixture.prepareProjectFiles();
    }
}
