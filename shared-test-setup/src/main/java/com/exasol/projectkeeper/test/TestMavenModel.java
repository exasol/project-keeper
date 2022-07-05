package com.exasol.projectkeeper.test;

import java.io.*;
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
    public static final String PROJECT_GROUP_ID = "com.exasol";
    private static final String PROJECT_DESCRIPTION = "My project description";

    public TestMavenModel() {
        this.setBuild(new Build());
        this.setVersion(PROJECT_VERSION);
        this.setArtifactId(PROJECT_ARTIFACT_ID);
        this.setName(PROJECT_NAME);
        this.setGroupId(PROJECT_GROUP_ID);
        this.setDescription(PROJECT_DESCRIPTION);
        this.setModelVersion("4.0.0");
    }

    public TestMavenModel withUrl(final String value) {
        setUrl(value);
        return this;
    }

    public TestMavenModel withGroupId(final String value) {
        setGroupId(value);
        return this;
    }

    public TestMavenModel withVersion(final String value) {
        setVersion(value);
        return this;
    }

    public TestMavenModel withDescription(final String value) {
        setDescription(value);
        return this;
    }

    public TestMavenModel addDependency() {
        return withDependency(DEFAULT_DEPENDENCY_VERSION);
    }

    public TestMavenModel withDependency(final String version) {
        return addDependency(DEPENDENCY_ARTIFACT_ID, DEPENDENCY_GROUP_ID, "", version);
    }

    public TestMavenModel addDependency(final String artifactId, final String groupId, final String scope,
            final String version) {
        final Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version);
        dependency.setScope(scope);
        return withDependency(dependency);
    }

    public TestMavenModel withDependency(final Dependency dependency) {
        this.addDependency(dependency);
        return this;
    }

    public TestMavenModel writeAsPomToProject(final Path projectDir) {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
            return this;
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public TestMavenModel configureAssemblyPluginFinalName() {
        final Plugin plugin = new Plugin();
        plugin.setArtifactId("maven-assembly-plugin");
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom finalName = new Xpp3Dom("finalName");
        finalName.setValue("my-jar");
        configuration.addChild(finalName);
        plugin.setConfiguration(configuration);
        return withPlugin(plugin);
    }

    public TestMavenModel withProjectKeeperPlugin(final String version) {
        final Plugin plugin = new Plugin();
        plugin.setGroupId("com.exasol");
        plugin.setArtifactId("project-keeper-maven-plugin");
        plugin.setVersion(version);
        return withPlugin(plugin);
    }

    public TestMavenModel withPlugin(final Plugin plugin) {
        this.getBuild().addPlugin(plugin);
        return this;
    }
}
