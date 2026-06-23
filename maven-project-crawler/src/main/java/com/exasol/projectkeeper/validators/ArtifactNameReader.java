package com.exasol.projectkeeper.validators;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * This class reads the file name of the release artifact produced by this pom.
 */
public class ArtifactNameReader {

    private static final String ASSEMBLY_PLUGIN_QUALIFIED_NAME = "org.apache.maven.plugins:maven-assembly-plugin";
    private final MavenProject project;

    /**
     * Create a new instance.
     * 
     * @param project the Maven project
     */
    public ArtifactNameReader(final MavenProject project) {
        this.project = project;
    }

    /**
     * Get the file names of the release artifacts relative to the {@code target} directory.
     *
     * @return file names relative to the {@code target} directory
     */
    public List<String> readArtifactNames() {
        return Optional.ofNullable(readFinalArtifactName())
                .map(this::releaseArtifactNames)
                .orElse(emptyList());
    }

    // [impl->dsn~customize-release-artifacts-jar~0]
    private String readFinalArtifactName() {
        return Optional.ofNullable(this.project.getBuild()
                .getPluginsAsMap()
                .get(ASSEMBLY_PLUGIN_QUALIFIED_NAME))
                .map(Plugin::getConfiguration) //
                .map(Xpp3Dom.class::cast) //
                .map(config -> config.getChild("finalName")) //
                .map(Xpp3Dom::getValue) //
                .map(artifactName -> artifactName + ".jar") //
                .orElse(null);
    }

    // [impl->dsn~customize-release-artifacts-sbom~0]
    private List<String> releaseArtifactNames(final String jarArtifactName) {
        final String spdxArtifactName = "site/" + this.project.getGroupId() + "_" + this.project.getArtifactId() + "-"
                + this.project.getVersion() + ".spdx.json";
        return List.of(jarArtifactName, spdxArtifactName);
    }
}
