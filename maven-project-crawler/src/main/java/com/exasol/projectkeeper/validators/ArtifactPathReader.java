package com.exasol.projectkeeper.validators;

import static java.util.Collections.emptyList;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * This class reads the artifact paths of the release artifacts produced by this pom.
 */
public class ArtifactPathReader {

    private static final String ASSEMBLY_PLUGIN_QUALIFIED_NAME = "org.apache.maven.plugins:maven-assembly-plugin";
    private final MavenProject project;

    /**
     * Create a new instance.
     * 
     * @param project the Maven project
     */
    public ArtifactPathReader(final MavenProject project) {
        this.project = project;
    }

    /**
     * Get the paths of the release artifacts relative to the {@code target} directory.
     *
     * @return artifact paths relative to the {@code target} directory
     */
    public List<Path> readArtifactPaths() {
        return Optional.ofNullable(readFinalArtifactName())
                .map(this::releaseArtifactPaths)
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
    private List<Path> releaseArtifactPaths(final String jarArtifactName) {
        final String spdxArtifactName = "site/" + this.project.getGroupId() + "." + this.project.getArtifactId()
                + "-" + this.project.getVersion() + ".spdx3.json";
        return List.of(Path.of(jarArtifactName), Path.of(spdxArtifactName));
    }
}
