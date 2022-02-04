package com.exasol.projectkeeper.validators.pom.plugin;

import com.exasol.projectkeeper.ProjectKeeperModule;

public class FlattenPluginValidator extends AbstractPluginPomValidator {
    public FlattenPluginValidator() {
        super("maven_templates/flatten-maven-plugin.xml");
    }

    @Override
    public ProjectKeeperModule getModule() {
        return ProjectKeeperModule.DEFAULT;
    }
}
