package com.exasol.projectkeeper.validators;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.exasol.projectkeeper.ProjectKeeperModule;

public class TestMavenModel extends Model {
    public final static String DEPENDENCY_GROUP_ID = "com.example";
    public final static String DEPENDENCY_ARTIFACT_ID = "my-lib";
    private static final String DEFAULT_DEPENDENCY_VERSION = "0.1.0";

    public TestMavenModel() {
        this.setBuild(new Build());
        this.setVersion("0.1.0");
        this.setArtifactId("my-test-project");
        this.setGroupId("com.example");
        this.setModelVersion("4.0.0");
    }

    public void addDependency() {
        addDependency(DEFAULT_DEPENDENCY_VERSION);
    }

    public void addDependency(final String version) {
        addDependency(DEPENDENCY_ARTIFACT_ID, DEPENDENCY_GROUP_ID, "", version);
    }

    public void addDependency(final String artifactId, final String groupId, final String scope, final String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        this.addDependency(dependency);
    }

    public void writeAsPomToProject(final Path projectDir) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
        }
    }

    public void addProjectKeeperPlugin(final Collection<ProjectKeeperModule> enabledModules, final String version) {
        final Plugin projectKeeperPlugin = new Plugin();
        projectKeeperPlugin.setGroupId("com.exasol");
        projectKeeperPlugin.setArtifactId("project-keeper-maven-plugin");
        projectKeeperPlugin.setVersion(version);
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom modules = buildModulesConfiguration(enabledModules);
        configuration.addChild(modules);
        projectKeeperPlugin.setConfiguration(configuration);
        final PluginExecution execution = new PluginExecution();
        execution.setGoals(List.of("verify"));
        projectKeeperPlugin.setExecutions(List.of(execution));
        this.getBuild().addPlugin(projectKeeperPlugin);
    }

    private Xpp3Dom buildModulesConfiguration(final Collection<ProjectKeeperModule> enabledModules) {
        final Xpp3Dom modules = new Xpp3Dom("modules");
        for (final ProjectKeeperModule enabledModule : enabledModules) {
            final Xpp3Dom module = new Xpp3Dom("module");
            module.setValue(enabledModule.name());
            modules.addChild(module);
        }
        return modules;
    }
}
