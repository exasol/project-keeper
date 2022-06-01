package com.exasol.projectkeeper.test;

import static java.util.Collections.emptySet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.ProjectKeeperConfigBuilder;
import com.exasol.projectkeeper.shared.config.ProjectKeeperConfig.SourceType;

public class GolangProjectFixture {
    private static final Duration PROCESS_TIMEOUT = Duration.ofSeconds(120);
    private static final String GO_MOD_FILE_NAME = "go.mod";
    private static final String GO_MODULE_NAME = "github.com/exasol/my-module";
    private static final String GO_VERSION = "1.17";
    private static final String PROJECT_VERSION = "1.2.3";

    private final Path projectDir;

    public GolangProjectFixture(final Path projectDir) {
        this.projectDir = projectDir;
    }

    public void gitInit() {
        try {
            Git.init().setDirectory(this.projectDir.toFile()).call().close();
        } catch (IllegalStateException | GitAPIException exception) {
            throw new AssertionError("Error running git init", exception);
        }
    }

    public ProjectKeeperConfig.ProjectKeeperConfigBuilder createDefaultConfig() {
        return ProjectKeeperConfig.builder()
                .sources(List.of(ProjectKeeperConfig.Source.builder().modules(emptySet()).type(SourceType.GOLANG)
                        .path(Path.of(GO_MOD_FILE_NAME)).build()))
                .versionConfig(new ProjectKeeperConfig.FixedVersion(PROJECT_VERSION));
    }

    public void prepareProjectFiles(final ProjectKeeperConfigBuilder configBuilder) {
        this.writeConfig(configBuilder);
        this.prepareProjectFiles();
    }

    public void prepareProjectFiles() {
        prepareProjectFiles(Paths.get("."));
    }

    public void prepareProjectFiles(final Path moduleDir) {
        writeGoModFile(moduleDir);
        writeMainGoFile(moduleDir);
        writeTestGoFile(moduleDir);
        splitAndExecute(moduleDir, "go get");
        splitAndExecute(moduleDir, "go mod tidy");
    }

    public String getProjectVersion() {
        return PROJECT_VERSION;
    }

    public void writeConfig(final ProjectKeeperConfig.ProjectKeeperConfigBuilder config) {
        new ProjectKeeperConfigWriter().writeConfig(config.build(), this.projectDir);
    }

    void initializeGitRepo() throws GitAPIException {
        Git.init().setDirectory(this.projectDir.toFile()).call().close();
    }

    private void writeGoModFile(final Path moduleDir) {
        final List<String> dependencies = List.of("github.com/exasol/exasol-driver-go v0.3.1",
                "github.com/exasol/exasol-test-setup-abstraction-server/go-client v0.0.0-20220520062645-0dd00179907c",
                "github.com/exasol/error-reporting-go v0.1.1 // indirect");
        final String content = "module " + GO_MODULE_NAME + "\n" //
                + "go " + GO_VERSION + "\n" //
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
        final String content = "package main\n" //
                + "import (\n" //
                + "    \"github.com/exasol/exasol-driver-go\"\n" //
                + ")\n" //
                + "func main() {\n" //
                + "    exasol.NewConfig(\"sys\", \"exasol\")\n" //
                + "}\n";
        writeFile(this.projectDir.resolve(moduleDir).resolve("main.go"), content);
    }

    private void writeTestGoFile(final Path moduleDir) {
        final String content = "package main\n" //
                + "import (\n" //
                + "    testSetupAbstraction \"github.com/exasol/exasol-test-setup-abstraction-server/go-client\"\n" //
                + ")\n" //
                + "func myTest() {\n" + "    exasol := testSetupAbstraction.Create(\"myConfig.json\")\n"
                + "    connection := exasol.CreateConnection()\n" + "}\n";
        writeFile(this.projectDir.resolve(moduleDir).resolve("main_test.go"), content);
    }

    private void splitAndExecute(final Path workingDir, final String command) {
        execute(workingDir, command.split(" "));
    }

    private void execute(final Path workingDir, final String... command) {
        try {
            final Process process = new ProcessBuilder(command) //
                    .directory(this.projectDir.resolve(workingDir).toFile()) //
                    .redirectErrorStream(false) //
                    .start();
            final boolean success = process.waitFor(PROCESS_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            if (!success) {
                final String stdOut = readStream(process.getInputStream());
                final String errorOut = readStream(process.getErrorStream());
                throw new AssertionError("Command " + Arrays.toString(command) + " did not finish after "
                        + PROCESS_TIMEOUT + ", std output: '" + stdOut + "', error output: '" + errorOut + "'.");
            }
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError(exception);
        }
    }

    private String readStream(final InputStream stream) throws IOException {
        return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
