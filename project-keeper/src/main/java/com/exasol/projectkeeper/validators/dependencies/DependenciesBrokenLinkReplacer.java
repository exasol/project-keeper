package com.exasol.projectkeeper.validators.dependencies;

import java.util.List;
import java.util.stream.Collectors;

import com.exasol.projectkeeper.BrokenLinkReplacer;
import com.exasol.projectkeeper.dependencies.License;
import com.exasol.projectkeeper.dependencies.ProjectDependency;

/**
 * This class replaces broken links in {@link ProjectDependency}s.
 * <p>
 * This is necessary since some maven projects have outdated / broken links in their pom file.
 * </p>
 */
public class DependenciesBrokenLinkReplacer {
    private final BrokenLinkReplacer brokenLinkReplacer;

    /**
     * Create a new instance of {@link DependenciesBrokenLinkReplacer}.
     * 
     * @param brokenLinkReplacer broken link replacer dependency injection
     */
    public DependenciesBrokenLinkReplacer(final BrokenLinkReplacer brokenLinkReplacer) {
        this.brokenLinkReplacer = brokenLinkReplacer;
    }

    /**
     * Replace broken links in a list of {@link ProjectDependency}s.
     * 
     * @param dependencies list of dependencies
     * @return list of dependencies with replaced links
     */
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
