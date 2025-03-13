package com.exasol.projectkeeper.sources.analyze.golang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class GoModFileTest {

    @Test
    void testParseEmptyFile() {
        final GoModFile file = GoModFile.parse("");
        assertModFile(file, null, null, 0, 0);
    }

    @ParameterizedTest
    @CsvSource({
            // Line comments
            "'// Line comment'", "'\t// Leading tab'", "'  // Leading spaces'", "'//NoSpaceAfterSlash'",
            "'// Trailing space '",
            // Replace directives
            "replace something with something", "replace\twith tab",
            "replace github.com/docker/docker => github.com/docker/docker v20.10.3-0 // 22.06 branch",
            // Replace block
            "'replace (\n" //
                    + "\tgithub.com/docker/docker => github.com/docker/docker v20.10.3-0.20221013203545-33ab36d6b304+incompatible // 22.06 branch\n"
                    + "more content\n" //
                    + ")'", })
    void testParseIgnoresLine(final String line) {
        final GoModFile file = GoModFile.parse(line);
        assertModFile(file, null, null, 0, 0);
    }

    @Test
    void testParseModuleName() {
        final GoModFile file = GoModFile.parse("module github.com/exasol/exasol-driver-go");
        assertModFile(file, "github.com/exasol/exasol-driver-go", null, 0, 0);
    }

    @Test
    void testParseGoVersion() {
        final GoModFile file = GoModFile.parse("go 1.17");
        assertModFile(file, null, "1.17", 0, 0);
    }

    @Test
    void testParseDependencyWithoutCommentIsDirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1");
        assertDependency(file, true, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void testParseDependencyWithUnknownCommentIsDirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // unknown");
        assertDependency(file, true, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void testParseDependencyWithIndirectCommentIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // indirect");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void testParseDependencyWithIndirectCommentNoSpacesIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1//indirect");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void testParseDependencyWithIndirectAndAdditionalCommentIsIndirect() {
        final GoModFile file = GoModFile.parse("github.com/exasol/error-reporting-go v0.1.1 // indirect more text");
        assertDependency(file, false, "github.com/exasol/error-reporting-go", "v0.1.1");
    }

    @Test
    void testParseSingleRequireLine() {
        final GoModFile file = GoModFile.parse("require github.com/exasol/exasol-driver-go v0.3.1");
        assertDependency(file, true, "github.com/exasol/exasol-driver-go", "v0.3.1");
    }

    @Test
    void testParseSingleRequireLineIndirect() {
        final GoModFile file = GoModFile.parse("require github.com/exasol/exasol-driver-go v0.3.1 // indirect");
        assertDependency(file, false, "github.com/exasol/exasol-driver-go", "v0.3.1");
    }

    @Test
    void testParseCompleteFile() throws IOException {
        final GoModFile file = GoModFile.parse(readResourceIntoString("/sample-contents-for-go.mod"));
        assertModFile(file, "github.com/exasol/exasol-driver-go", "1.17", 35, 4);
    }

    private String readResourceIntoString(final String resource) throws IOException {
        try (InputStream stream = GoModFileTest.class.getResourceAsStream(resource)) {
            return new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining("\n"));
        }
    }

    @Test
    void testParseFailsForUnexpectedLines() throws IOException {
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

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(GoModFile.class).verify();
    }

    @Test
    void testToString() {
        ToStringVerifier.forClass(GoModFile.class).verify();
    }
}
