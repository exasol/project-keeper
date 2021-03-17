package com.exasol.projectkeeper.validators;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

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
        final Dependency dependency = new Dependency();
        dependency.setGroupId(DEPENDENCY_GROUP_ID);
        dependency.setArtifactId(DEPENDENCY_ARTIFACT_ID);
        dependency.setVersion(version);
        dependency.setScope("");
        this.addDependency(dependency);
    }

    public void writeAsPomToProject(final Path projectDir) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(projectDir.resolve("pom.xml").toFile())) {
            new MavenXpp3Writer().write(fileWriter, this);
        }
    }
}
