package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.Collections.emptyList;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.*;
import com.exasol.projectkeeper.sources.analyze.generic.CommandExecutor;
import com.exasol.projectkeeper.sources.analyze.generic.ShellCommand;

import jakarta.json.JsonObject;

public class NpmDependencies {

    private final CommandExecutor executor;
    private final PackageJson packageJson;
    private final Path projectDir;
    private JsonObject dependencies;
    private Map<String, List<NpmLicense>> licenseMap;

    NpmDependencies(final CommandExecutor executor, final PackageJson packageJson) {
        this.executor = executor;
        this.packageJson = packageJson;
        this.projectDir = packageJson.getProjectDir();
    }

//    NpmDependencies(final CommandExecutor executor, final Path projectDir) {
//        this.executor = executor;
//        this.projectDir = projectDir;
//    }

    List<ProjectDependency> getDependencies() {
        // TODO:
        // 1. evaluate dependencies from packageJson
        // 2. differentiate between COMPILE and PLUGIN dependencies
        // 3. use output of command NpmCommand.LIST_DEPENDENCIES only in second step in order to retrieve additional
        // information such as the URL
        final List<VersionedDependency> dep = this.packageJson.getDependencies();
        this.dependencies = getJsonOutput(this.projectDir, NpmCommand.LIST_DEPENDENCIES) //
                .getJsonObject("dependencies");
        this.licenseMap = retrieveNpmLicenses();
        return this.dependencies.keySet().stream() //
                .map(this::projectDependency) //
                .collect(Collectors.toList());
    }

    private Map<String, List<NpmLicense>> retrieveNpmLicenses() {
        final JsonObject json = getJsonOutput(this.projectDir, NpmCommand.LICENSE_CHECKER);
        return json.keySet().stream() //
                .map(key -> NpmLicense.from(this.projectDir, key, json))
                .collect(Collectors.toMap(NpmLicense::getModule, List::of));
    }

    private ProjectDependency projectDependency(final String module) {
        return ProjectDependency.builder() //
                .name(module) //
                .websiteUrl(getUrl(module)) //
                .type(ProjectDependency.Type.COMPILE) // TODO
                .licenses(moduleLicenses(module)) //
                .build();
    }

    private List<License> moduleLicenses(final String module) {
        return Optional.ofNullable(this.licenseMap.get(module)) //
                .orElse(emptyList()) //
                .stream() //
                .map(NpmLicense::toLicense) //
                .collect(Collectors.toList());
    }

    private String getUrl(final String module) {
        return this.dependencies.getJsonObject(module).getString("resolved");
    }

    private JsonObject getJsonOutput(final Path workingDirectory, final ShellCommand cmd) {
        return PackageJsonReader.read(this.executor.execute(cmd, workingDirectory));
    }
}
