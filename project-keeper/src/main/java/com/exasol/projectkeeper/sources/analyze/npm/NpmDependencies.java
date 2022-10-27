package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.Collections.emptyList;

import java.io.StringReader;
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
    private JsonObject additionalInfo;
    private Map<String, List<NpmLicense>> licenseMap;

    NpmDependencies(final CommandExecutor executor, final PackageJson packageJson) {
        this.executor = executor;
        this.packageJson = packageJson;
    }

    /**
     * <ul>
     * <li>evaluate dependencies from packageJson</li>
     * <li>differentiate between COMPILE and PLUGIN dependencies</li>
     * <li>use output of command NpmCommand.LIST_DEPENDENCIES only in second step in order to retrieve additional
     * information such as the URL</li>
     * </ul>
     *
     * @return list of project dependencies with type, name, website URL, and licenses
     */
    List<ProjectDependency> getDependencies() {
        this.additionalInfo = getJsonOutput(NpmCommand.LIST_DEPENDENCIES) //
                .getJsonObject("dependencies");
        this.licenseMap = retrieveNpmLicenses();
        return this.packageJson.getDependencies().stream() //
                .map(this::projectDependency) //
                .collect(Collectors.toList());
    }

    private Map<String, List<NpmLicense>> retrieveNpmLicenses() {
        final JsonObject json = getJsonOutput(NpmCommand.LICENSE_CHECKER);
        return json.keySet().stream() //
                .map(key -> NpmLicense.from(key, json)).collect(Collectors.toMap(NpmLicense::getModule, List::of));
    }

    private ProjectDependency projectDependency(final VersionedDependency versionedDependency) {
        final String moduleName = versionedDependency.getName();
        return ProjectDependency.builder() //
                .name(moduleName) //
                .websiteUrl(getUrl(moduleName)) //
                .type(versionedDependency.getType()) //
                .licenses(moduleLicenses(moduleName)) //
                .build();
    }

    private List<License> moduleLicenses(final String moduleName) {
        return Optional.ofNullable(this.licenseMap.get(moduleName)) //
                .orElse(emptyList()) //
                .stream() //
                .map(NpmLicense::toLicense) //
                .collect(Collectors.toList());
    }

    private String getUrl(final String moduleName) {
        return this.additionalInfo.getJsonObject(moduleName).getString("resolved");
    }

    private JsonObject getJsonOutput(final ShellCommand cmd) {
        final String stdout = this.executor.execute(cmd, getWorkingDir());
        return JsonIo.read(new StringReader(stdout));
    }

    private Path getWorkingDir() {
        final Path path = this.packageJson.getPath();
        return path == null ? null : path.getParent();
    }
}
