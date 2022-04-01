package com.exasol.projectkeeper;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.ProjectKeeperConfigBuilder;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.sources.analyze.golang.SimpleProcess;

@Tag("integration")
class ProjectKeeperGolangIT extends ProjectKeeperAbstractIT {

    private static final String PROJECT_VERSION = "1.2.3";
    private static final String GO_MODULE_NAME = "github.com/exasol/my-module";
    private static final String GO_VERSION = "1.17";

    @Test
    void testVerifyPhase1() throws IOException {
        prepareProjectFiles(createConfig());
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-17: Missing required file: 'release_config.yml'"));
    }

    @Test
    void fixingSolvesAllIssues() throws IOException {
        prepareProjectFiles(createConfig());
        runFix();
        assertVerifySucceeds();
        assertGeneratedFiles();
    }

    private void prepareProjectFiles(final ProjectKeeperConfigBuilder config) throws IOException {
        writeGoModFile();
        writeMainGoFile();
        writeConfig(config.build());
        executeSplit("go mod tidy");
    }

    private void assertGeneratedFiles() throws IOException {
        final String dependencies = Files.readString(this.projectDir.resolve("dependencies.md"));
        final String changelog = Files
                .readString(this.projectDir.resolve("doc/changes/changes_" + PROJECT_VERSION + ".md"));
        assertThat(changelog, allOf(containsString("* Added `golang:1.17`"),
                containsString("* Added `github.com/exasol/exasol-driver-go:v0.3.1`")));
        assertThat(dependencies, containsString("github.com/exasol/exasol-driver-go | [MIT][0] "));
    }

    @Test
    void testVerifyPhase2() throws IOException {
        prepareProjectFiles(createConfig());
        runFix();
        Files.delete(this.projectDir.resolve(".gitignore"));
        final String output = assertInvalidAndGetOutput();
        assertThat(output, containsString("E-PK-CORE-56: Could not find required file '.gitignore'."));
    }

    private ProjectKeeperConfig.ProjectKeeperConfigBuilder createConfig() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(emptySet()).type(SourceType.GOLANG)
                        .path(Path.of("go.mod")).build()))
                .versionConfig(new ProjectKeeperConfig.FixedVersion(PROJECT_VERSION));
    }

    private void writeGoModFile() throws IOException {
        final List<String> dependencies = List.of("github.com/exasol/exasol-driver-go v0.3.1",
                "github.com/exasol/error-reporting-go v0.1.1 // indirect");
        final String content = "module " + GO_MODULE_NAME + "\n" //
                + "go " + GO_VERSION + "\n" //
                + "require (\n" //
                + "\t" + String.join("\n\t", dependencies) + "\n" //
                + ")\n";
        Files.writeString(this.projectDir.resolve("go.mod"), content);
    }

    private void writeMainGoFile() throws IOException {
        final String content = "package main\n" + "import (\n" + "    \"github.com/exasol/exasol-driver-go\"\n" + ")\n"
                + "func main() {\n" + "    exasol.NewConfig(\"sys\", \"exasol\")\n" + "}\n";
        Files.writeString(this.projectDir.resolve("main.go"), content);
    }

    private void executeSplit(final String command) {
        execute(command.split(" "));
    }

    private void execute(final String... command) {
        SimpleProcess.start(this.projectDir, asList(command)).getOutput(Duration.ofSeconds(10));
    }
}
