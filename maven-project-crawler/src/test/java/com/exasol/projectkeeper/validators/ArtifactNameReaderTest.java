package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.jupiter.api.Test;

class ArtifactNameReaderTest {

    @Test
    void readArtifactNameMissingPlugin() {
        assertThat(readArtifactNames(new MavenProject()), empty());
    }

    // [utest->dsn~customize-release-artifacts-jar~0]
    // [utest->dsn~customize-release-artifacts-sbom~0]
    @Test
    void readArtifactNames() {
        final MavenProject project = new MavenProject();
        project.setGroupId("com.exasol");
        project.setArtifactId("my-project");
        project.setVersion("1.2.3");
        project.getBuild().addPlugin(createPlugin("artifact-name"));
        assertThat(readArtifactNames(project), contains("artifact-name.jar", "site/com.exasol_my-project-1.2.3.spdx.json"));
    }

    private List<String> readArtifactNames(final MavenProject project) {
        return new ArtifactNameReader(project).readArtifactNames();
    }

    private Plugin createPlugin(final String finalName) {
        final Plugin plugin = new Plugin();
        plugin.setGroupId("org.apache.maven.plugins");
        plugin.setArtifactId("maven-assembly-plugin");
        final Xpp3Dom finalNameElement = new Xpp3Dom("finalName");
        finalNameElement.setValue(finalName);
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        configuration.addChild(finalNameElement);
        plugin.setConfiguration(configuration);
        return plugin;
    }
}
