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

    static final ShellCommand LIST_DEPENDENCIES = ShellCommand.builder() //
            .command(NPM, "list") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--depth=0", "--json") //
            .build();

    static final ShellCommand LICENSE_CHECKER = ShellCommand.builder() //
            .command(NPX, "license-checker") //
            .timeout(Duration.ofMinutes(2)) //
            .args("--location=project", "--direct", "--json") //
            .build();

    private final CommandExecutor executor;
    private final GitService git;

    NpmServices(final CommandExecutor executor, final GitService git) {
        this.executor = executor;
        this.git = git;
    }

    PackageJson readPackageJson(final Path path) {
        return PackageJsonReader.read(path);
    }

    ProjectDependencies getDependencies(final PackageJson packageJson) {
        return new ProjectDependencies(new NpmDependencies(this, packageJson).getDependencies());
    }

    Optional<PackageJson> retrievePrevious(final Path projectDir, final PackageJson current) {
        return new PreviousRelease(this.git) //
                .projectDir(projectDir) //
                .currentVersion(current.getVersion()) //
                .file(projectDir.relativize(current.getPath())) //
                .getContent() //
                .map(string -> PackageJsonReader.read(current.getPath(), string));
    }

    JsonObject listDependencies(final Path folder) {
        return getJsonOutput(LIST_DEPENDENCIES, folder);
    }

    JsonObject getLicenses(final Path folder) {
        return getJsonOutput(LICENSE_CHECKER, folder);
    }

    private JsonObject getJsonOutput(final ShellCommand cmd, final Path workingDir) {
        final String stdout = this.executor.execute(cmd, workingDir);
        return JsonIo.read(new StringReader(stdout));
    }
}
