package com.exasol.projectkeeper.validators;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class TestMavenModel extends Model {
    private static final long serialVersionUID = -4204751250626409157L;
    public static final String DEPENDENCY_GROUP_ID = "com.example";
    public static final String DEPENDENCY_ARTIFACT_ID = "my-lib";
    public static final String PROJECT_ARTIFACT_ID = "my-test-project";
    public static final String PROJECT_NAME = "my test project";
    public static final String PROJECT_VERSION = "0.1.0";
    private static final String DEFAULT_DEPENDENCY_VERSION = "0.1.0";
    public static final String PROJECT_GROUP_ID = "com.example";

    public TestMavenModel() {
        this.setBuild(new Build());
        this.setVersion(PROJECT_VERSION);
        this.setArtifactId(PROJECT_ARTIFACT_ID);
        this.setName(PROJECT_NAME);
        this.setGroupId(PROJECT_GROUP_ID);
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
}
