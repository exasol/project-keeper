package com.exasol.projectkeeper.plugin;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

public class MvnProjectWithProjectKeeperPluginWriter extends Model {
    private static final long serialVersionUID = -8757020322006895512L;
    public static final String PROJECT_ARTIFACT_ID = "my-test-project";
    public static final String PROJECT_VERSION = "0.1.0";

    public MvnProjectWithProjectKeeperPluginWriter(final String projectKeeperVersion) {
        this.setBuild(new Build());
        this.setVersion(PROJECT_VERSION);
        this.setArtifactId(PROJECT_ARTIFACT_ID);
        this.setGroupId("com.exasol");
        this.setModelVersion("4.0.0");
        this.setDescription("my project description");
        addProjectKeeperPlugin(projectKeeperVersion);
    }

    public void writeAsPomToProject(final Path projectDir) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
        }
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
