package com.exasol.projectkeeper.validators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.jupiter.api.Test;

class ArtifactNameReaderTest {

    @Test
    void readArtifactNameMissingPlugin() {
        assertThat(readArtifactName(new MavenProject()), nullValue());
    }

    // [utest->dsn~customize-release-artifacts-jar~0]
    @Test
    void readArtifactName() {
        final MavenProject project = new MavenProject();
        project.getBuild().addPlugin(createPlugin("artifact-name"));
        assertThat(readArtifactName(project), equalTo("artifact-name.jar"));
    }

    private String readArtifactName(final MavenProject project) {
        return new ArtifactNameReader(project).readFinalArtifactName();
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
