package com.exasol.projectkeeper.sources.analyze.npm;

import java.io.StringReader;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

import com.exasol.projectkeeper.OsCheck;
import com.exasol.projectkeeper.shared.dependencies.ProjectDependencies;
import com.exasol.projectkeeper.sources.analyze.generic.*;

import jakarta.json.JsonObject;

class NpmServices {

    private static final String NPM = "npm" + OsCheck.suffix(".cmd");
    private static final String NPX = "npx" + OsCheck.suffix(".cmd");

    private static final ShellCommand LIST_DEPENDENCIES = ShellCommand.builder() //
            .command(NPM, "list") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--depth=0", "--json") //
            .build();

    private static final ShellCommand LICENSE_CHECKER = ShellCommand.builder() //
            .command(NPX, "license-checker") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--direct", "--json") //
            .build();

    private final CommandExecutor executor;

    NpmServices(final CommandExecutor executor) {
        this.executor = executor;
    }

    ProjectDependencies getDependencies(final PackageJson packageJson) {
        return new ProjectDependencies(new NpmDependencies(this, packageJson).getDependencies());
    }

    Optional<PackageJson> retrievePrevious(final Path projectDir, final PackageJson current) {
        return PreviousRelease.from(projectDir, current.getVersion()) //
                .fileContent(current.getPath()) //
                .map(string -> PackageJsonReader.read(current.getPath(), string));
    }

    JsonObject listDependencies(final Path packageJsonPath) {
        return getJsonOutput(LIST_DEPENDENCIES, workingDir(packageJsonPath));
    }

    JsonObject getLicenses(final Path packageJsonPath) {
        return getJsonOutput(LICENSE_CHECKER, workingDir(packageJsonPath));
    }

    public JsonObject getJsonOutput(final ShellCommand cmd, final Path workingDir) {
        final String stdout = this.executor.execute(cmd, workingDir);
        return JsonIo.read(new StringReader(stdout));
    }

    Path workingDir(final Path path) {
        return path == null ? null : path.getParent();
    }

}
