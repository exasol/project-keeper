package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class GoModFileTest {

    @Test
    void parseEmptyFile() {
        final GoModFile file = GoModFile.parse("");
        assertModFile(file, null, null, 0, 0);
    }

    @Test
    void parseModuleName() {
        final GoModFile file = GoModFile.parse("module github.com/exasol/exasol-driver-go");
        assertModFile(file, "github.com/exasol/exasol-driver-go", null, 0, 0);
    }

    @Test
    void parseGoVersion() {
        final GoModFile file = GoModFile.parse("go 1.17");
        assertModFile(file, null, "1.17", 0, 0);
    }

    @Test
    void parseDependencyWithoutCommentIsDirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1");
        assertDependency(file, true, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void parseDependencyWithUnknownCommentIsDirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // unknown");
        assertDependency(file, true, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void parseDependencyWithIndirectCommentIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // indirect");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void parseDependencyWithIndirectCommentNoSpacesIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1//indirect");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void parseDependencyWithIndirectAndAdditionalCommentIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // indirect more text");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void parseSingleRequireLine() {
        final GoModFile file = GoModFile.parse("require github.com/exasol/exasol-driver-go v0.3.1");
        assertDependency(file, true, "github.com/exasol/exasol-driver-go", "v0.3.1");
    }

    @Test
    void parseSingleRequireLineIndirect() {
        final GoModFile file = GoModFile.parse("require github.com/exasol/exasol-driver-go v0.3.1 // indirect");
        assertDependency(file, false, "github.com/exasol/exasol-driver-go", "v0.3.1");
    }

    @Test
    void parseCompleteFile() throws IOException {
        final GoModFile file = GoModFile.parse(Files.readString(Path.of("src/test/resources/go.mod")));
        assertModFile(file, "github.com/exasol/exasol-driver-go", "1.17", 35, 4);
    }

    @Test
    void failsForUnexpectedLines() throws IOException {
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> GoModFile.parse("unexpected"));
        assertThat(exception.getMessage(), equalTo("E-PK-CORE-138: Found unexpected line 'unexpected' in go.mod file"));
    }

    private void assertDependency(final GoModFile file, final boolean direct, final String expectedName,
            final String expectedVersion) {
        assertAll( //
                () -> assertThat("all dependencies", file.getDependencies(), hasSize(1)),
                () -> assertThat("direct dependencies", file.getDirectDependencies(), hasSize(direct ? 1 : 0)),
                () -> assertThat("dependency name", file.getDependencies().get(0).getName(), equalTo(expectedName)),
                () -> assertThat("dependency version", file.getDependencies().get(0).getVersion(),
                        equalTo(expectedVersion))

        );
    }

    private void assertModFile(final GoModFile file, final String expectedModuleName, final String expectedGoVersion,
            final int expectedDependencies, final int expectedDirectDependencies) {
        assertAll( //
                () -> assertThat("module name", file.getModuleName(), equalTo(expectedModuleName)),
                () -> assertThat("go version", file.getGoVersion(), equalTo(expectedGoVersion)),
                () -> assertThat("dependencies", file.getDependencies(), hasSize(expectedDependencies)),
                () -> assertThat("direct dependencies", file.getDirectDependencies(),
                        hasSize(expectedDirectDependencies)));
    }
}
