package com.exasol.projectkeeper.plugin;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MvnProjectWithProjectKeeperPluginWriter extends Model {
    public static final String PROJECT_ARTIFACT_ID = "my-test-project";
    public static final String PROJECT_VERSION = "0.1.0";

    public MvnProjectWithProjectKeeperPluginWriter(final String projectKeeperVersion) {
        this.setBuild(new Build());
        this.setVersion(PROJECT_VERSION);
        this.setArtifactId(PROJECT_ARTIFACT_ID);
        this.setGroupId("com.example");
        this.setModelVersion("4.0.0");
        addProjectKeeperPlugin(projectKeeperVersion);
    }

    public void writeAsPomToProject(final Path projectDir) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
        }
    }

    public void configureAssemblyPluginFinalName() {
        final Plugin assemblyPlugin = new Plugin();
        assemblyPlugin.setArtifactId("maven-assembly-plugin");
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom finalName = new Xpp3Dom("finalName");
        finalName.setValue("my-jar");
        configuration.addChild(finalName);
        assemblyPlugin.setConfiguration(configuration);
        this.getBuild().addPlugin(assemblyPlugin);
    }

    private void addProjectKeeperPlugin(final String version) {
        final Plugin projectKeeperPlugin = new Plugin();
        projectKeeperPlugin.setGroupId("com.exasol");
        projectKeeperPlugin.setArtifactId("project-keeper-maven-plugin");
        projectKeeperPlugin.setVersion(version);
        final PluginExecution execution = new PluginExecution();
        execution.setGoals(List.of("verify"));
        projectKeeperPlugin.setExecutions(List.of(execution));
        this.getBuild().addPlugin(projectKeeperPlugin);
    }
}
