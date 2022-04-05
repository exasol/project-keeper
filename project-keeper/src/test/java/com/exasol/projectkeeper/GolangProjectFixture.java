package com.exasol.projectkeeper;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.config.ProjectKeeperConfig.SourceType;
import com.exasol.projectkeeper.config.ProjectKeeperConfigWriter;
import com.exasol.projectkeeper.sources.analyze.golang.SimpleProcess;

class GolangProjectFixture {
    private static final String GO_MODULE_NAME = "github.com/exasol/my-module";
    private static final String GO_VERSION = "1.17";
    private static final String PROJECT_VERSION = "1.2.3";

    private final Path projectDir;

    GolangProjectFixture(final Path projectDir) {
        this.projectDir = projectDir;
    }

    ProjectKeeperConfig.ProjectKeeperConfigBuilder createDefaultConfig() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(emptySet()).type(SourceType.GOLANG)
                        .path(Path.of("go.mod")).build()))
                .versionConfig(new ProjectKeeperConfig.FixedVersion(PROJECT_VERSION));
    }

    void prepareProjectFiles() throws IOException {
        writeGoModFile();
        writeMainGoFile();
        splitAndExecute("go mod tidy");
    }

    String getProjectVersion() {
        return PROJECT_VERSION;
    }

    void writeConfig(final ProjectKeeperConfig.ProjectKeeperConfigBuilder config) {
        new ProjectKeeperConfigWriter().writeConfig(config.build(), this.projectDir);
    }

    void initializeGitRepo() throws GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
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

    private void splitAndExecute(final String command) {
        execute(command.split(" "));
    }

    private void execute(final String... command) {
        SimpleProcess.start(this.projectDir, asList(command)).getOutput(Duration.ofSeconds(60));
    }
}
