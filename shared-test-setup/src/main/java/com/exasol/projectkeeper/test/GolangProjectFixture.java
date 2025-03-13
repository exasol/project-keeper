package com.exasol.projectkeeper.test;

import static java.util.Collections.emptySet;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.exasol.projectkeeper.shared.config.*;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.Builder;

public class GolangProjectFixture extends BaseProjectFixture {

    private static final String GO_MOD_FILE_NAME = "go.mod";
    private static final String GO_MODULE_NAME = "github.com/exasol/my-module";
    private static final String GO_VERSION = "1.17";
    private static final String PROJECT_VERSION = "1.2.3";

    public GolangProjectFixture(final Path projectDir) {
        super(projectDir);
    }

    public ProjectKeeperConfig.Builder createDefaultConfig() {
        return ProjectKeeperConfig.builder().sources(List.of(
                Source.builder().modules(emptySet()).type(SourceType.GOLANG).path(Path.of(GO_MOD_FILE_NAME)).build()))
                .versionConfig(new FixedVersion(PROJECT_VERSION));
    }

    public void prepareProjectFiles(final Builder configBuilder) {
        this.writeConfig(configBuilder);
        this.prepareProjectFiles();
    }

    public void prepareProjectFiles() {
        prepareProjectFiles(Path.of("."), GO_VERSION);
    }

    public void prepareProjectFiles(final Path moduleDir, final String goVersion) {
        writeGoModFile(moduleDir, goVersion);
        writeMainGoFile(moduleDir);
        writeTestGoFile(moduleDir);
        execute(projectDir.resolve(moduleDir), "go", "get");
        execute(projectDir.resolve(moduleDir), "go", "mod", "tidy");
    }

    public String getProjectVersion() {
        return PROJECT_VERSION;
    }

    private void writeGoModFile(final Path moduleDir, final String goVersion) {
        final List<String> dependencies = List.of("github.com/exasol/exasol-driver-go v0.4.3",
                "github.com/exasol/exasol-test-setup-abstraction-server/go-client v0.2.2",
                "github.com/exasol/error-reporting-go v0.1.1 // indirect");
        final String content = "module " + GO_MODULE_NAME + "\n" //
                + "go " + goVersion + "\n" //
                + "require (\n" //
                + "\t" + String.join("\n\t", dependencies) + "\n" //
                + ")\n";
        writeFile(this.projectDir.resolve(moduleDir).resolve(GO_MOD_FILE_NAME), content);
    }

    private void writeFile(final Path path, final String content) {
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Error writing content to file " + path, exception);
        }
    }

    private void writeMainGoFile(final Path moduleDir) {
        final String content = """
                package main
                import (
                    "github.com/exasol/exasol-driver-go"
                )
                func main() {
                    exasol.NewConfig("sys", "exasol")
                }
                """;
        writeFile(this.projectDir.resolve(moduleDir).resolve("main.go"), content);
    }

    private void writeTestGoFile(final Path moduleDir) {
        final String content = """
                package main
                import (
                    testSetupAbstraction "github.com/exasol/exasol-test-setup-abstraction-server/go-client"
                )
                func myTest() {
                    exasol := testSetupAbstraction.Create("myConfig.json")
                    connection := exasol.CreateConnection()
                }
                """;
        writeFile(this.projectDir.resolve(moduleDir).resolve("main_test.go"), content);
    }
}
