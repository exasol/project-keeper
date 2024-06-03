package com.exasol.projectkeeper.sources.analyze.npm;

import static java.util.Collections.emptyList;

import java.util.*;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.shared.dependencies.*;

import jakarta.json.JsonObject;

class NpmDependencies {

    private final NpmServices services;
    private final PackageJson packageJson;
    private JsonObject additionalInfo;
    private Map<String, List<NpmLicense>> licenseMap;

    NpmDependencies(final NpmServices services, final PackageJson packageJson) {
        this.services = services;
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
        this.additionalInfo = this.services.listDependencies(this.packageJson.getWorkingDir())
                .getJsonObject("dependencies");
        this.licenseMap = retrieveNpmLicenses();
        return this.packageJson.getDependencies().stream() //
                .map(this::projectDependency) //
                .collect(Collectors.toList());
    }

    private Map<String, List<NpmLicense>> retrieveNpmLicenses() {
        final JsonObject json = this.services.getLicenses(this.packageJson.getWorkingDir());
        return json.keySet().stream() //
                .map(key -> NpmLicense.from(key, json)) //
                .collect(Collectors.toMap(NpmLicense::getModule, List::of));
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
        return Optional.ofNullable(this.additionalInfo.getJsonObject(moduleName)) //
                .map(object -> object.getString("resolved")) //
                .orElse(null);
    }
}
