package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.BrokenLinkReplacer;

public class DependenciesBrokenLinkReplacer {
    private final BrokenLinkReplacer brokenLinkReplacer;

    public DependenciesBrokenLinkReplacer(final BrokenLinkReplacer brokenLinkReplacer) {
        this.brokenLinkReplacer = brokenLinkReplacer;
    }

    public List<ProjectDependency> replaceBrokenLinks(final List<ProjectDependency> dependencies) {
        return dependencies.stream().map(this::replaceBrokenLinks).collect(Collectors.toList());
    }

    private ProjectDependency replaceBrokenLinks(final ProjectDependency dependency) {
        return new ProjectDependency(dependency.getName(),
                this.brokenLinkReplacer.replaceIfBroken(dependency.getWebsiteUrl()),
                replaceBrokenLicenseLinks(dependency.getLicenses()), dependency.getType());
    }

    private List<License> replaceBrokenLicenseLinks(final List<License> licenses) {
        return licenses.stream().map(this::replaceBrokenLicenseLinks).collect(Collectors.toList());
    }

    private License replaceBrokenLicenseLinks(final License license) {
        return new License(license.getName(), this.brokenLinkReplacer.replaceIfBroken(license.getUrl()));
    }
}
