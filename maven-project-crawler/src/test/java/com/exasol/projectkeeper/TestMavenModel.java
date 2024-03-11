package com.exasol.projectkeeper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class TestMavenModel extends Model {
    private static final long serialVersionUID = -554596503224484792L;
    public static final String PROJECT_ARTIFACT_ID = "my-test-project";
    public static final String PROJECT_NAME = "my test project";
    public static final String PROJECT_VERSION = "0.1.0";
    public static final String PROJECT_GROUP_ID = "com.example";

    public TestMavenModel() {
        this.setBuild(new Build());
        this.setVersion(PROJECT_VERSION);
        this.setArtifactId(PROJECT_ARTIFACT_ID);
        this.setName(PROJECT_NAME);
        this.setGroupId(PROJECT_GROUP_ID);
        this.setModelVersion("4.0.0");
    }

    public void addDependency(final String artifactId, final String groupId, final String scope, final String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        this.addDependency(dependency);
    }

    public TestMavenModel configureAssemblyPluginFinalName(final String finalName) {
        final Plugin plugin = new Plugin();
        plugin.setArtifactId("maven-assembly-plugin");
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom finalNameElement = new Xpp3Dom("finalName");
        finalNameElement.setValue(finalName);
        configuration.addChild(finalNameElement);
        plugin.setConfiguration(configuration);
        return withPlugin(plugin);
    }

    public TestMavenModel withPlugin(final Plugin plugin) {
        this.getBuild().addPlugin(plugin);
        return this;
    }

    public void setJavaVersionProperty(final String javaVersion) {
        this.addProperty("java.version", javaVersion);
    }

    public void writeAsPomToProject(final Path projectDir) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
        }
    }
}
