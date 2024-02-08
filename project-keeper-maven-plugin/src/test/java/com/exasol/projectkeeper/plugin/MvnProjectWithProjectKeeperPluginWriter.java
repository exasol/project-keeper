package com.exasol.projectkeeper.plugin;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.*;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import com.exasol.projectkeeper.validators.pom.PomFileIO;

public class MvnProjectWithProjectKeeperPluginWriter {
    public static final String PROJECT_ARTIFACT_ID = "my-test-project";
    public static final String PROJECT_VERSION = "0.1.0";
    private final Model model;

    public MvnProjectWithProjectKeeperPluginWriter(final String projectKeeperVersion) {
        this.model = new Model();
        this.model.setBuild(new Build());
        this.model.setVersion(PROJECT_VERSION);
        this.model.setArtifactId(PROJECT_ARTIFACT_ID);
        this.model.setGroupId("com.exasol");
        this.model.setModelVersion("4.0.0");
        this.model.setDescription("my project description");
        addProjectKeeperPlugin(projectKeeperVersion);
    }

    public void writeAsPomToProject(final Path projectDir) {
        final Path path = projectDir.resolve("pom.xml");
        new PomFileIO().writePom(model, path);
    }

    public MvnProjectWithProjectKeeperPluginWriter addDependency(final String groupId, final String artifactId,
            final String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        this.model.getDependencies().add(dependency);
        return this;
    }

    public MvnProjectWithProjectKeeperPluginWriter setArtifactFinalName(final String finalName) {
        final Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-assembly-plugin");
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom finalNameElement = new Xpp3Dom("finalName");
        finalNameElement.setValue(finalName);
        configuration.addChild(finalNameElement);
        plugin.setConfiguration(configuration);
        this.model.getBuild().addPlugin(plugin);
        return this;
    }

    private void addProjectKeeperPlugin(final String version) {
        final Plugin projectKeeperPlugin = new Plugin();
        projectKeeperPlugin.setGroupId("com.exasol");
        projectKeeperPlugin.setArtifactId("project-keeper-maven-plugin");
        projectKeeperPlugin.setVersion(version);
        final PluginExecution execution = new PluginExecution();
        execution.setGoals(List.of("verify"));
        projectKeeperPlugin.setExecutions(List.of(execution));
        this.model.getBuild().addPlugin(projectKeeperPlugin);
    }
}
