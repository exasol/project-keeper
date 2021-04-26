package com.exasol.projectkeeper.validators;

import java.util.Arrays;
import java.util.List;

import com.exasol.projectkeeper.ProjectKeeperModule;

public class ProjectKeeperPluginDeclaration {
    private final String version;
    private List<ProjectKeeperModule> enabledModules;
    private List<String> excludedFiles;
    private List<String> excludedPlugins;
    private List<String> linkReplacements;

    public ProjectKeeperPluginDeclaration(final String version) {
        this.version = version;
    }

    public ProjectKeeperPluginDeclaration withEnabledModules(final ProjectKeeperModule... enabledModules) {
        return this.withEnabledModules(Arrays.asList(enabledModules));
    }

    public ProjectKeeperPluginDeclaration withEnabledModules(final List<ProjectKeeperModule> enabledModules) {
        this.enabledModules = enabledModules;
        return this;
    }

    public ProjectKeeperPluginDeclaration withExcludedFiles(final String... excludedFiles) {
        return this.withExcludedFiles(Arrays.asList(excludedFiles));
    }

    public ProjectKeeperPluginDeclaration withExcludedFiles(final List<String> excludedFiles) {
        this.excludedFiles = excludedFiles;
        return this;
    }

    public ProjectKeeperPluginDeclaration withExcludedPlugins(final String... excludedPlugins) {
        return this.withExcludedPlugins(Arrays.asList(excludedPlugins));
    }

    public ProjectKeeperPluginDeclaration withExcludedPlugins(final List<String> excludedPlugins) {
        this.excludedPlugins = excludedPlugins;
        return this;
    }

    public ProjectKeeperPluginDeclaration withLinkReplacements(final String... linkReplacements) {
        return this.withLinkReplacements(Arrays.asList(linkReplacements));
    }

    public ProjectKeeperPluginDeclaration withLinkReplacements(final List<String> linkReplacements) {
        this.linkReplacements = linkReplacements;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public List<ProjectKeeperModule> getEnabledModules() {
        return this.enabledModules;
    }

    public List<String> getExcludedFiles() {
        return this.excludedFiles;
    }

    public List<String> getExcludedPlugins() {
        return this.excludedPlugins;
    }

    public List<String> getLinkReplacements() {
        return this.linkReplacements;
    }
}
