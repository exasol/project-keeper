package com.exasol.projectkeeper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.model.Plugin;

import com.exasol.projectkeeper.validators.TestMavenModel;

public class TestMavenSetupWithParentPom {

    public void writeTo(final Path projectDirectory) throws IOException {
        writeParentPom(projectDirectory);
        final Path pomFile = projectDirectory.resolve("pom.xml");
        Files.writeString(pomFile, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
                + "<project xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n"
                + "    <modelVersion>4.0.0</modelVersion>\n"//
                + "    <artifactId>my-project</artifactId>\n"//
                + "    <packaging>jar</packaging>\n"//
                + "    <parent>\n"//
                + "        <relativePath>./parent/pom.xml</relativePath>\n"//
                + "        <artifactId>my-parent</artifactId>\n"//
                + "        <groupId>" + TestMavenModel.PROJECT_GROUP_ID + "</groupId>\n"//
                + "        <version>" + TestMavenModel.PROJECT_VERSION + "</version>\n"//
                + "    </parent>"//
                + "</project>");
    }

    private void writeParentPom(final Path tempDir) throws IOException {
        final TestMavenModel parentPom = new TestMavenModel();
        parentPom.setPackaging("pom");
        final Plugin plugin = new Plugin();
        plugin.setGroupId("com.exasol");
        plugin.setArtifactId("error-code-crawler-maven-plugin");
        plugin.setVersion("0.7.1");
        parentPom.getBuild().addPlugin(plugin);
        parentPom.setArtifactId("my-parent");
        final Path dirForParentPom = tempDir.resolve("parent");
        Files.createDirectory(dirForParentPom);
        parentPom.writeAsPomToProject(dirForParentPom);
    }
}
